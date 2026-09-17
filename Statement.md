# Problem Statement & Project Scope

## Problem Statement
Small to mid-sized retail electronics stores face significant challenges in managing product inventory, computing taxes (18% GST), applying store promotional discounts (10-15%), and maintaining permanent record-keeping. Manual bookkeeping leads to stock discrepancies, calculation errors during customer checkout, and lost historical sales data.

## Project Scope
The **Electronics Billing & Inventory System** is a standalone Java Swing desktop application integrated with a MySQL relational database. The system automates key operational workflows:
- Managing stock levels with automatic deduplication.
- Processing real-time sales transactions with (10-15)% auto-discount and 18% GST calculation.
- Storing customer invoices permanently in MySQL.
- Providing deletion options for inventory products and past orders.

## Target Users
- **Store Owners & Managers:** To track current stock, add new items, and delete outdated orders.
- **Billing Cashiers:** To quickly generate bills and hand over displayed receipts as well as printed receipts to customers.

## High-Level Features
- Graphical User Interface (GUI) powered by Java Swing.
- Persistent storage using MySQL via JDBC architecture.
- Real-time stock quantity incrementing on existing items.
- Itemized billing with auto-discount and GST.
- Order history viewing and selective record deletion.
