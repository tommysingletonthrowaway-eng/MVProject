# CLI Banking Application

A simple **Java command-line banking app** that manages users, their bank accounts, and transaction history — all saved using encrypted serialization.

---

## Features

- **User management**
    - Create new users with username and password
    - Delete existing users
    - User login authentication
    - Change username and password when logged in

- **Bank account management**
    - Create and delete multiple bank accounts per user
    - Each account has a **name** and an **active currency**
    - Deposit and withdraw funds
    - View account balance
    - Change currency per account (with currency conversion)

- **Transaction history**
    - Stores and serializes transaction history per bank account

- **Data persistence**
    - All users, bank accounts, and transactions are serialized and saved to disk
    - Ability to **reset all saved data** from the main menu
    - Data is encrypted with SimpleXOR encryption

- **Menu system**
    - Intuitive CLI navigation with three menus:
        - `MainMenu` (login, user creation, reset data)
        - `UserMenu` (user settings, manage accounts)
        - `AccountMenu` (account operations like deposit, withdraw, view balance)

- **Unit tested**
    - Core functionality including user management, bank accounts, transactions, and currency conversion are covered by unit tests to ensure reliability and ease of maintenance.

---

## How It Works

- Data is serialized to `.dat` files for persistence between sessions.
- On startup, users are prompted to log in or create an account.
- Once logged in, users can manage their bank accounts and perform transactions.
- Currency conversion allows managing accounts in different currencies.
- The app provides safeguards such as login authentication and data reset confirmation.

---

## Getting Started

### Prerequisites

- Java JDK 8 or higher
- Maven (optional, if you use it for building)

### Running the Application

1. Clone the repository
2. Build the project using your IDE or via Maven:
   ```bash
   mvn clean compile
