# MusicStore — POO Java Project

## Description
Catalogue management application for a music shop.
Project developed for the course **Programmazione ad Oggetti** (Unimore).

---

## Project structure

```
MusicStore/
├── src/
│   └── musicstore/
│       ├── Main.java                    ← Main entrance point
│       ├── model/
│       │   ├── Product.java             ← Clase abstracta (root)
│       │   ├── Album.java               ← Product subclass
│       │   ├── Instrument.java          ← Product subclass
│       │   ├── Accessory.java           ← Product subclass
│       │   └── Shop.java                ← Generic class Shop<T extends Product>
│       ├── gui/
│       │   ├── MainWindow.java          ← Main window (Swing)
│       │   └── DialogProduct.java       ← Add/Edit product dialog (Swing)
│       ├── storage/
│       │   └── GestorFicheros.java      ← I/O files CSV
│       └── util/
│           └── ProductoTableModel.java  ← Swing TableModel for JTable
├── out/                                 ← .class files
├── doc/                                 ← Generated Javadoc
├── compilar.sh                          ← Script Linux/Mac
```

---

## POO concepts applied

### Encapsulation
- All fields are private, access through public getters/setters
- Hidden logic inside classes (e.g. CSV parsing in `FilesManager`)

### Inheritance
- `Product` (abstract) → `Album`, `Instrument`, `Accessory`
- `ProductTableModel` extends `AbstractTableModel`

### Abstraction
- `Product` defines abstract methods: `getCategory()`, `getDetailedDescription()`, `toCSV()`

### Polymorphism
- `List<Product>` can contain Album, Instrument o Accessory
- Polymorphism calls: `p.getDetailedDescription()`, `p.toCSV()`, `p.getCategory()`

### Generics
- `Shop<T extends Product>` generic class

### GUI
- Swing: `JFrame`, `JTable`, `JDialog`, `JMenuBar`, `JFileChooser`
- Main window + dialog for add/edit product

### I/O (Reader/Writer)
- `BufferedWriter` + `FileWriter` → save CSV
- `BufferedReader` + `FileReader` → load CSV
- `PrintWriter` → export report in text format

### Packages
- `musicstore` — principal package
- `musicstore.model` — domain model (Product, Shop)
- `musicstore.gui` — GUI (MainWindow, DialogProduct)
- `musicstore.storage` — persistence (FilesManager)
- `musicstore.util` — utilities (ProductTableModel)

---

## Compilation and execution

### Linux / Mac
```bash
chmod u+x compilar.sh
./compilar.sh
```

### Manual
```bash
find src -name "*.java" > sources.txt
javac -encoding UTF-8 -d out @sources.txt
java -cp out musicstore.Main
rm sources.txt
```

### Generate Javadoc
```bash
find src -name "*.java" > sources.txt
javadoc -encoding UTF-8 -d doc -sourcepath src -subpackages musicstore @sources.txt
rm sources.txt
```

## Requirements
At least, should be launched and compiled using a 21 version or higher of Java:

```bash
sudo apt install openjdk-21-jdk
```

---
