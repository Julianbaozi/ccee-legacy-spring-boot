clear;

Set-ExecutionPolicy -Scope CurrentUser -ExecutionPolicy Bypass -Force;
Set-ItemProperty 'HKLM:\System\CurrentControlSet\Control\FileSystem' -Name 'LongPathsEnabled' -value 1;

$logEnable = 'false'

Function Log() {

    Param($mensagem)

    if ($logEnable -eq 'TRUE') {
        $mensagem
    }
}

Function Copia-Permissoes-Entrada {

    Param($item)

    $tipo_item = "Arquivo";
    if ($item.PSIsContainer) {
        $tipo_item = "Pasta"
    }

    Log("Avaliando a item '" + $item.FullName + "' e seu tipo é " + $tipo_item)

    if ($item.PSIsContainer) {
        #consulta todos os arquivos a partir da pasta raiz que foi indicada
        $filhos = Get-ChildItem $item.FullName -Force

        #executa o comportamento de validação e copia de grupos para cada arquivo e pasta encontrados
        ForEach ($filho in $filhos) {
            Copia-Permissoes-Entrada($filho)
        }
    }

    #consulta informações de permissões da pasta
    $acesso = Get-Acl $item.FullName

    #variavel de controle para identificar se houve criação de nova regra
    $existe_nova_regra = 'FALSE'

    #para cada permissão da pasta elegivel, faz copia para novo dominio
    ForEach ($regra in $acesso.Access) {
        $nome_grupo = $regra.IdentityReference.value
        
        Log('Avaliando grupo ' + $nome_grupo + ' - sua marcação de herança é ' + $regra.IsInherited)

        #verifica se o grupo pertence ao dominio de origem e se a permissão não é herdada da pasta pai.
        #caso a permissão seja herdada, então não precisa copiar, por que a pasta pai já tera a permissão e consequentemente a pasta/arquivo em questão também
        if ($nome_grupo.StartsWith($dominio_origem) -And -not $regra.IsInherited ){
            
            #faz a troca do dominio de origem, pelo dominio destino
            $novo_grupo = $nome_grupo.Replace($dominio_origem,$dominio_destino)
            
            Log('O grupo ' + $nome_grupo + ' será alterado para ' + $novo_grupo)
            
            #faz tratamento de erro para lidar com permissões que não existem no destino
            try {
                #cria objeto da nova regra
                $nova_regra = New-Object System.Security.AccessControl.FileSystemAccessRule($novo_grupo, $regra.FileSystemRights, $regra.InheritanceFlags, $regra.PropagationFlags, $regra.AccessControlType)
            
                #adiciona na lista de permissões da pasta/arquivo a nova regra para o grupo do dominio destino
                $acesso.SetAccessRule($nova_regra)
                
                Log('Grupo ' + $novo_grupo + ' aculmulado na lista de acesso com sucesso')
                
                #indicando que houve criação de regra
                $existe_nova_regra = 'TRUE'
            
            } catch {
                'Erro ao atribuir permissão para pasta ' + $nome_item + ' -> Permissão nao existe no destino ' + $novo_grupo
            }
        }

    }

    #caso alguma nova regra tenha sido criada para pasta, efetiva alterações
    if ($existe_nova_regra -eq 'TRUE') {
        'Efetivando alterações na pasta ' + $nome_item
        $acesso | Set-Acl $nome_item
    }

}

#total de parametros passado
$total_argumentos = [int] $args.Count

if ($total_argumentos -eq 3) {
    #variavel para indicar qual o dominio de origem dos grupos que devem ser copiados com um novo dominio.
    $dominio_origem = $args[0] + "\"
    'Dominio dos grupos que serão copiados -> ' + $dominio_origem
    #variavel com o nome do novo dominio dos grupos que serão copiados.
    $dominio_destino = $args[1] + "\"
    'Dominio dos grupos novos -> ' + $dominio_destino

    $pasta_raiz = $args[2]
    'Pasta que será avaliada -> ' + $pasta_raiz
} else {
    Write-Output "Passe o primeiro parametro como o dominio de origem dos grupos que serão copiados."
    Write-Output "Passe o segundo parametro como o dominio de destino dos grupos listados."
    Write-Output "Passe o terceiro parametro como a pasta raiz que será lida recursivamente para adicionar novas permissões."
    exit;
}

#Avalia a pasta indicada como referencia
$root_folder = Get-Item -LiteralPath $pasta_raiz -Force
Copia-Permissoes-Entrada($root_folder)
