# NTT-DATA-Java-e-IA-para-Iniciantes-Transacoes-Financeiras-com-POO
# Banco Digital com Programação Orientada a Objetos em Java

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)

## 📜 Descrição do Projeto

Este projeto é uma aplicação de console que simula um sistema bancário digital, desenvolvido em Java com foco na aplicação prática dos conceitos de **Programação Orientada a Objetos (POO)**. A versão atual evoluiu para um sistema mais robusto onde a entidade central é o **Cliente**, que pode possuir múltiplas contas (Corrente e Poupança) e uma carteira de investimentos consolidada.

O sistema permite gerenciar clientes, contas e um ciclo de vida completo de investimentos, com uma interface interativa que fornece feedback detalhado para cada operação realizada pelo usuário.

## ✨ Funcionalidades Principais

* **Gestão de Clientes**:
    * Criação de novos clientes (contatos) no sistema.
    * Listagem de todos os clientes cadastrados com suas respectivas contas e saldos.

* **Gestão de Contas**:
    * Adição de contas do tipo `Corrente` e `Poupança` para clientes existentes.
    * O sistema impede que um cliente tenha mais de uma conta do mesmo tipo.

* **Operações Bancárias Clássicas**:
    * **Depósito e Saque**: Com seleção de conta (Corrente ou Poupança) caso o cliente possua mais de uma.
    * **Transferência**: Movimentação de valores entre quaisquer duas contas do sistema.
    * **Histórico de Transações**: Consulta de extrato detalhado por conta.

* **Sistema de Investimentos Completo**:
    * **Aplicação**: Permite investir um valor, debitando-o do saldo de uma conta.
    * **Carteira de Investimentos**: Cada cliente possui sua própria carteira, que agrupa todas as suas aplicações.
    * **Listagem e Consulta**: Visualização detalhada da carteira de um cliente, com o valor atual de cada investimento.
    * **Resgate**: Permite "sacar" um investimento, creditando seu valor atualizado em uma conta à escolha do cliente.
    * **Simulação de Rendimentos**: Uma função que aplica um rendimento percentual aleatório a todos os investimentos ativos no sistema, simulando a valorização dos ativos.

* **Interface Informativa**:
    * O sistema fornece **feedback claro e detalhado** após cada operação, informando valores, saldos atualizados e status da transação.

## 🧠 Conceitos de POO e Boas Práticas Aplicados

O projeto foi estruturado para aplicar os pilares da POO e seguir boas práticas de desenvolvimento de software:

* **Abstração**: A classe `Conta` é `abstract`, definindo um modelo comum para contas.
* **Encapsulamento**: Atributos são `private` ou `protected`, com acesso controlado por métodos públicos, garantindo a integridade dos dados.
* **Herança**: As classes `ContaCorrente` e `ContaPoupanca` herdam de `Conta`, promovendo o reuso de código.
* **Polimorfismo**: O método `imprimirExtrato()` é sobrescrito (`@Override`) para customizar a saída de cada tipo de conta.
* **Composição**: A classe `Cliente` é composta por uma lista de `Contas` e uma `CarteiraInvestimento`, modelando a relação "tem-um".

### Outras Práticas Adotadas:

* **Separação de Responsabilidades (Arquitetura em Camadas)**:
    * `ui`: Camada de interação com o usuário (`MenuConsole`).
    * `service`: Camada de regras de negócio (`BancoService`).
    * `repository`: Camada de acesso a dados (`ClienteRepository`).
    * `model`, `enums`, `records`: Camadas de domínio e dados.
* **Lombok**: Para reduzir código boilerplate (`@Getter`, `@Setter`).
* **Java Records**: Para classes de dados imutáveis como `Transacao`.
* **Java Enums**: Para tipos seguros como `TipoTransacao` e `StatusInvestimento`.

## 📂 Estrutura do Projeto

```
/src/main/java/org/example/
├── ui/
│   └── MenuConsole.java          # Interface do usuário (console)
├── service/
│   └── BancoService.java         # Camada de regras de negócio
├── repository/
│   └── ClienteRepository.java      # Simulação de persistência de clientes
├── model/
│   ├── Cliente.java              # Entidade principal
│   ├── Conta.java                # Classe abstrata base
│   ├── ContaCorrente.java
│   ├── ContaPoupanca.java
│   ├── Investimento.java
│   └── CarteiraInvestimento.java # Agrupador de investimentos
├── records/
│   └── Transacao.java            # DTO imutável para transações
├── enums/
│   ├── TipoTransacao.java
│   └── StatusInvestimento.java   # Status de um investimento
└── Main.java                     # Ponto de entrada da aplicação
```

## 🚀 Como Executar o Projeto

### Pré-requisitos
* JDK 17 ou superior.
* Git.

### Passos

1.  **Clone o repositório:**
    ```bash
    git clone [URL-DO-SEU-REPOSITORIO-NO-GITHUB]
    ```

2.  **Navegue até o diretório do projeto:**
    ```bash
    cd Transacoes-Financeiras-com-POO
    ```

3.  **Execute a aplicação usando o Gradle Wrapper** (ele cuidará de baixar as dependências e compilar o projeto):
    * No Windows:
        ```bash
        .\gradlew.bat run
        ```
    * No Linux ou macOS:
        ```bash
        ./gradlew run
        ```

## 🖼️ Exemplo de Uso da Versão Atualizada

A interação com o usuário agora é mais detalhada, como mostram os exemplos abaixo.

**Criando uma conta e recebendo feedback imediato:**
```
>> 2. ADICIONAR CONTA
CPF do cliente: 12345678900
Tipo da conta (corrente/poupanca): corrente
Conta criada com sucesso!
Agência: 0001, Conta: 0001
```

**Depositando com seleção de conta:**
```
>> 3. DEPOSITAR
CPF do titular da conta: 12345678900
Este cliente possui mais de uma conta. Selecione a conta para depósito:
[0] - Corrente (Conta: 0001)
[1] - Poupança (Conta: 0002)
Digite o índice da conta: 0
Valor a depositar: 500
Depósito de R$500,00 realizado com sucesso. Novo saldo: R$500,00
```

**Realizando um investimento:**
```
>> INVESTIMENTO: APLICAR
CPF do cliente: 12345678900
Este cliente possui mais de uma conta. Selecione a conta de onde sairá o valor:
[0] - Corrente (Conta: 0001)
[1] - Poupança (Conta: 0002)
Digite o índice da conta: 0
Nome do investimento (Ex: CDB, Ações XYZ): Ações da DIO
Valor a investir: 200
Investimento em 'Ações da DIO' no valor de R$200,00 realizado com sucesso.
Saldo atual da conta: R$150,00
```
