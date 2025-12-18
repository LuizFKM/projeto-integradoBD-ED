# 🚀 Sistema de Gerenciamento de Estacionamento

## 💻 Sobre o projeto

Sistema Desktop para **gerenciamento de estacionamento**, desenvolvido como projeto integrado no 4º Período de Sistemas de Informação do **IFPR** (Banco de Dados II e Programação III).

O objetivo foi criar uma solução para controle de fluxo de veículos, integrando uma interface Java Swing com banco de dados relacional.

### Funcionalidades Principais

* **Gestão de Cadastros:** CRUD completo de Clientes e Veículos (com relacionamento).
* **Controle de Operações:** Registro de entrada/saída, cálculo de permanência e tarifação por hora.
* **Visualização de vagas:** JPane que possui uma JTable com informções e filtros entre vagas livres/ocupadas.
* **Visualização de Dados:** Listagem de registros utilizando componentes `JTable`.
* **Relatório:** Geração de relatório em PDF com **JasperReports**, exibindo:
    * Vaga utilizada.
    * Quantidade de operações na vaga.
    * Tempo total de ocupação e faturamento diário.
    * *Obs: Dados processados diretamente no banco via PostgreSQL Views.*

## 🛠 Tecnologias

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![JasperReports](https://img.shields.io/badge/JasperReports-brightgreen?style=for-the-badge)

> O projeto utiliza **Java Swing** para a interface desktop.

## ⚙️ Como executar

Clone o projeto e acesse a pasta:

```bash
git clone [https://github.com/LuizFKM/projeto-integradoBD-ED.git](https://github.com/LuizFKM/projeto-integradoBD-ED.git)
cd projeto-integradoBD-ED
```

### Configuração do Banco de Dados

O projeto depende de um banco de dados **PostgreSQL** com regras de negócio e permissões (Roles) pré-definidas.

1. Crie um banco de dados chamado `estacionamento`.
2. Execute o script SQL 'Estacionamento.sql' disponível no repositorio para criar as tabelas, views e roles.
3. **Importante:** É necessário cadastrar manualmente as vagas iniciais na tabela `vaga` para que o sistema funcione.
4. O arquivo 'scriptFciticio' neste mesmo repositório é um script com algumas inserções e queries para teste.

#### 🔐 Níveis de Acesso (Database Roles)

O sistema utiliza o próprio controle de acesso do PostgreSQL. Para logar na aplicação, utilize um dos usuários abaixo, dependendo do nível de permissão desejado:

| Role | Login | Senha | Permissões |
| :--- | :--- | :--- | :--- |
| **Admin** | `admin` | `admin123` | Acesso total (CRUD completo em todas as tabelas). |
| **Colaborador** | `colaborador` | `colab123` | Pode registrar entradas/saídas e cadastrar clientes. **Não pode deletar** registros. |

---

## 🧠 Arquitetura do Banco de Dados

O banco de dados não serve apenas como repositório; ele contém regras de negócio essenciais para a integridade do sistema.

### Automação (Triggers e Functions)
As seguintes funções são disparadas automaticamente pelo banco e refletidas na aplicação Java:
* `fn_atualizar_status_da_vaga`: Altera o status da vaga (Livre/Ocupada) automaticamente ao registrar entrada ou saída.
* `fn_bloquear_vaga_ocupada`: Impede via Trigger (`BEFORE INSERT`) que o sistema tente registrar um carro em uma vaga que já consta como ocupada.

### Visualização e Relatórios (Views)
* **`vw_informacoes_vaga`**: Utilizada na aba de gestão de vagas da interface Desktop. Alimenta o painel de detalhes e a `JTable`, permitindo filtrar vagas por ID ou status.
* **`vw_informacoes_vaga_jasper`**: View otimizada exclusivamente para a geração do relatório PDF via **JasperReports**. Ela pré-calcula o tempo total formatado e o faturamento acumulado por vaga.

### Outros recursos
O banco também possui *Materialized Views* para faturamento mensal e *Stored Procedures* para realizar checkout e cálculos financeiros, acessíveis para administração direta via banco.

## 📂 Arquitetura e Estrutura do Projeto

O projeto segue o padrão de arquitetura **MVC (Model-View-Controller)** combinado com o padrão **DAO (Data Access Object)**, garantindo que a regra de negócio, a interface visual e a persistência de dados estejam desacopladas e organizadas. A estrutura de pastas segue o padrão oficial do **Apache Maven**.

### Árvore de Pacotes
```bash
src/main/java/com/luizfrancisco/estacionamento/
│
├── controller/  # Camada de controle (Regras de negócio e chamadas ao DAO)
├── dao/         # Data Access Object (Queries SQL e comunicação direta com o BD)
├── database/    # Configuração da conexão JDBC com PostgreSQL
├── model/       # Classes representando as entidades (Cliente, Veículo, etc.)
├── util/        # Utilitários (Gerador de Relatórios e Validadores)
└── view/        # Interface Gráfica (Java Swing)

```
### Detalhes dos Componentes

* **Controller & DAO:** Os *Controllers* atuam como intermediários. Eles recebem os dados da View, aplicam validações e chamam os métodos do *DAO* para executar as operações de CRUD no banco.
* **Database:** O pacote `database` centraliza a conexão JDBC, facilitando a manutenção das credenciais do banco em um único lugar.
* **Utils (Relatórios e Validação):**
  * `RelatorioUtil`: Classe responsável por gerar relatórios Jasper. **Diferencial:** O relatório é renderizado dentro da própria janela da aplicação, sem a necessidade de abrir leitores de PDF externos.
  * `ValidadorDeCampos`: Garante que dados obrigatórios não sejam enviados em branco ou com formato inválido.
* **Views (Interface):**
  * `Principal.java`: Tela mestre que utiliza `JTabbedPane` para navegar entre as 4 abas principais: **Clientes, Veículos, Operações e Vagas**.
  * `BuscarClientes` e `BuscarVeiculo`: Telas modais de apoio para vincular registros durante os cadastros.

---

## 🔒 Regras de Negócio e Integridade

Para garantir a consistência dos dados, o sistema implementa travas lógicas (Integridade Referencial):

1. **Proteção de Veículos:** Não é possível excluir um veículo que possua histórico de operações (ativas ou finalizadas). Isso preserva o histórico financeiro.
2. **Proteção de Clientes:** Um cliente não pode ser removido do sistema se possuir veículos vinculados ao seu cadastro.

> Essas regras são validadas tanto na camada de aplicação (Java) quanto na camada de banco de dados (Foreign Key Constraints).
