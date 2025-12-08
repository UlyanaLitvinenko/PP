import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.*;
import org.xml.sax.SAXException;


public class LibraryApp extends JFrame {

    private File currentXmlFile = new File("C:/Users/user/IdeaProjects/XML/src/library.xml");
    private File currentXsdFile = new File("C:/Users/user/IdeaProjects/XML/src/library.xsd");



    private final LibraryModel model = new LibraryModel();
    private final JTable table = new JTable(model);



    public LibraryApp() {
        super("Library XML Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 540);
        setLocationRelativeTo(null);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);

        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        setJMenuBar(createMenuBar());
    }

    private JMenuBar createMenuBar() {
        JMenuBar mb = new JMenuBar();

        JMenu file = new JMenu("File");
        JMenuItem load = new JMenuItem("Load XML + Validate");
        JMenuItem save = new JMenuItem("Save XML");
        JMenuItem exit = new JMenuItem("Exit");

        load.addActionListener(e -> onLoad());
        save.addActionListener(e -> onSave());
        exit.addActionListener(e -> System.exit(0));

        file.add(load);
        file.add(save);
        file.addSeparator();
        file.add(exit);

        JMenu actions = new JMenu("Actions");
        JMenuItem addBook = new JMenuItem("Add Book");
        JMenuItem search = new JMenuItem("Search");
        JMenuItem changePrice = new JMenuItem("Change Price");
        JMenuItem issue = new JMenuItem("Issue Book");

        addBook.addActionListener(e -> onAddBook());
        search.addActionListener(e -> onSearch());
        changePrice.addActionListener(e -> onChangePrice());
        issue.addActionListener(e -> onIssue());

        actions.add(addBook);
        actions.add(search);
        actions.add(changePrice);
        actions.add(issue);

        mb.add(file);
        mb.add(actions);
        return mb;
    }

    private void onLoad() {
        try {
            validateXml(currentXmlFile, currentXsdFile);
            List<Book> books = readXml(currentXmlFile);

            for (Book b : books) {
                if (b.available < 0 || b.copies < 0 || b.available > b.copies) {
                    throw new IllegalArgumentException("Integrity error for id=" + b.id);
                }
            }

            model.setBooks(books);
            JOptionPane.showMessageDialog(this, "XML loaded and validated successfully. Books: " + books.size());
        } catch (Exception ex) {
            showError("Failed to load/validate: " + ex.getMessage());
        }
    }

    private void onSave() {
        try {
            writeXml(currentXmlFile, model.getBooks());
            if (currentXsdFile != null) validateXml(currentXmlFile, currentXsdFile);
            JOptionPane.showMessageDialog(this, "Saved: " + currentXmlFile.getAbsolutePath());
        } catch (Exception ex) {
            showError("Failed to save: " + ex.getMessage());
        }
    }


    private void onAddBook() {
        Book b = promptBook(null);
        if (b == null) return;

        if (model.getBooks().stream().anyMatch(x -> x.id.equals(b.id))) {
            showError("Book with id=" + b.id + " already exists.");
            return;
        }
        if (b.available < 0 || b.copies < 0 || b.available > b.copies) {
            showError("available must be between 0 and copies.");
            return;
        }
        if (b.price.compareTo(BigDecimal.ZERO) < 0) {
            showError("Price must be >= 0.");
            return;
        }

        model.addBook(b);
    }

    private void onSearch() {
        String[] options = {"By author", "By year", "By category"};
        String choice = (String) JOptionPane.showInputDialog(this, "Search type:", "Search",
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (choice == null) return;

        List<Book> result = new ArrayList<>();
        switch (choice) {
            case "By author": {
                String author = JOptionPane.showInputDialog(this, "Author contains:");
                if (author == null) return;
                result = model.getBooks().stream()
                        .filter(b -> b.author.toLowerCase().contains(author.toLowerCase()))
                        .collect(Collectors.toList());
                break;
            }
            case "By year": {
                String yearStr = JOptionPane.showInputDialog(this, "Year (YYYY):");
                if (yearStr == null) return;
                try {
                    int y = Integer.parseInt(yearStr.trim());
                    result = model.getBooks().stream().filter(b -> b.year == y).collect(Collectors.toList());
                } catch (NumberFormatException ex) {
                    showError("Invalid year.");
                    return;
                }
                break;
            }
            case "By category": {
                String cat = JOptionPane.showInputDialog(this, "Category contains:");
                if (cat == null) return;
                result = model.getBooks().stream()
                        .filter(b -> b.category.toLowerCase().contains(cat.toLowerCase()))
                        .collect(Collectors.toList());
                break;
            }
        }
        showResults(result);
    }

    private void onChangePrice() {
        int row = table.getSelectedRow();
        if (row < 0) {
            showError("Select a book in the table.");
            return;
        }
        row = table.convertRowIndexToModel(row);
        Book b = model.getBookAt(row);

        String priceStr = JOptionPane.showInputDialog(this, "New price for \"" + b.title + "\":", b.price.toPlainString());
        if (priceStr == null) return;
        try {
            BigDecimal p = new BigDecimal(priceStr.trim());
            if (p.compareTo(BigDecimal.ZERO) < 0) throw new NumberFormatException();
            b.price = p;
            model.fireTableRowsUpdated(row, row);
        } catch (NumberFormatException ex) {
            showError("Invalid price.");
        }
    }

    private void onIssue() {
        int row = table.getSelectedRow();
        if (row < 0) {
            showError("Select a book in the table.");
            return;
        }
        row = table.convertRowIndexToModel(row);
        Book b = model.getBookAt(row);

        if (b.available <= 0) {
            showError("No available copies to issue.");
            return;
        }
        b.available -= 1;
        model.fireTableRowsUpdated(row, row);
        JOptionPane.showMessageDialog(this, "Issued 1 copy. Available now: " + b.available);
    }

    private void showResults(List<Book> result) {
        if (result.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No matches.");
            return;
        }
        LibraryModel tempModel = new LibraryModel();
        tempModel.setBooks(result);
        JTable t = new JTable(tempModel);
        t.setAutoCreateRowSorter(true);
        JScrollPane sp = new JScrollPane(t);
        sp.setPreferredSize(new Dimension(860, 320));
        JOptionPane.showMessageDialog(this, sp, "Search results (" + result.size() + ")", JOptionPane.PLAIN_MESSAGE);
    }

    private Book promptBook(Book preset) {
        JTextField id = new JTextField(preset == null ? "" : preset.id);
        JTextField title = new JTextField(preset == null ? "" : preset.title);
        JTextField author = new JTextField(preset == null ? "" : preset.author);
        JTextField year = new JTextField(preset == null ? "" : String.valueOf(preset.year));
        JTextField price = new JTextField(preset == null ? "" : preset.price.toPlainString());
        JTextField category = new JTextField(preset == null ? "" : preset.category);
        JTextField copies = new JTextField(preset == null ? "" : String.valueOf(preset.copies));
        JTextField available = new JTextField(preset == null ? "" : String.valueOf(preset.available));

        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.add(new JLabel("id (string/unique):")); panel.add(id);
        panel.add(new JLabel("title:")); panel.add(title);
        panel.add(new JLabel("author:")); panel.add(author);
        panel.add(new JLabel("year (YYYY):")); panel.add(year);
        panel.add(new JLabel("price (decimal):")); panel.add(price);
        panel.add(new JLabel("category:")); panel.add(category);
        panel.add(new JLabel("copies (>=0):")); panel.add(copies);
        panel.add(new JLabel("available (>=0 <= copies):")); panel.add(available);

        int ok = JOptionPane.showConfirmDialog(this, panel, "Add Book", JOptionPane.OK_CANCEL_OPTION);
        if (ok != JOptionPane.OK_OPTION) return null;

        try {
            String sid = id.getText().trim();
            String stitle = title.getText().trim();
            String sauthor = author.getText().trim();
            int syear = Integer.parseInt(year.getText().trim());
            BigDecimal sprice = new BigDecimal(price.getText().trim());
            String scategory = category.getText().trim();
            int scopies = Integer.parseInt(copies.getText().trim());
            int savailable = Integer.parseInt(available.getText().trim());

            if (sid.isEmpty() || stitle.isEmpty() || sauthor.isEmpty() || scategory.isEmpty())
                throw new IllegalArgumentException("Fields cannot be empty.");
            if (sprice.compareTo(BigDecimal.ZERO) < 0)
                throw new IllegalArgumentException("Price must be >= 0.");
            if (scopies < 0 || savailable < 0 || savailable > scopies)
                throw new IllegalArgumentException("available must be between 0 and copies.");

            return new Book(sid, stitle, sauthor, syear, scategory, sprice, scopies, savailable);
        } catch (Exception ex) {
            showError("Invalid input: " + ex.getMessage());
            return null;
        }
    }

    private void validateXml(File xml, File xsd) throws Exception {
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = sf.newSchema(xsd);
        Validator validator = schema.newValidator();
        try {
            validator.validate(new StreamSource(xml));
        } catch (SAXException e) {
            throw new Exception("XML is not valid: " + e.getMessage(), e);
        }
    }

    private List<Book> readXml(File xmlFile) throws Exception {
        List<Book> books = new ArrayList<>();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(xmlFile);
        doc.getDocumentElement().normalize();

        NodeList nodes = doc.getElementsByTagName("book");
        for (int i = 0; i < nodes.getLength(); i++) {
            Element e = (Element) nodes.item(i);

            String id = e.getAttribute("id");
            String copiesAttr = e.getAttribute("copies");
            String availableAttr = e.getAttribute("available");

            int copies = parseIntSafe(copiesAttr, "copies", id);
            int available = parseIntSafe(availableAttr, "available", id);

            String title = getText(e, "title");
            String author = getText(e, "author");
            int year = Integer.parseInt(getText(e, "year").trim());
            BigDecimal price = new BigDecimal(getText(e, "price").trim());
            String category = getText(e, "category");

            books.add(new Book(id, title, author, year, category, price, copies, available));
        }
        return books;
    }

    private int parseIntSafe(String value, String field, String id) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid integer in '" + field + "' for book id=" + id);
        }
    }

    private String getText(Element parent, String tag) {
        NodeList nl = parent.getElementsByTagName(tag);
        if (nl.getLength() == 0) return "";
        return nl.item(0).getTextContent().trim();
    }

    private void writeXml(File xmlFile, List<Book> books) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();

        Element root = doc.createElement("library");
        doc.appendChild(root);

        for (Book b : books) {
            Element e = doc.createElement("book");
            e.setAttribute("id", b.id);
            e.setAttribute("copies", String.valueOf(b.copies));
            e.setAttribute("available", String.valueOf(b.available));

            append(doc, e, "title", b.title);
            append(doc, e, "author", b.author);
            append(doc, e, "year", String.valueOf(b.year));
            append(doc, e, "price", b.price.toPlainString());
            append(doc, e, "category", b.category);

            root.appendChild(e);
        }

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = tf.newTransformer();
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.setOutputProperty(OutputKeys.METHOD, "xml");
        t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        t.transform(new DOMSource(doc), new StreamResult(xmlFile));
    }

    private void append(Document doc, Element parent, String tag, String text) {
        Element child = doc.createElement(tag);
        child.setTextContent(text);
        parent.appendChild(child);
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LibraryApp().setVisible(true));
    }


    static class Book {
        String id;
        String title;
        String author;
        int year;
        String category;
        BigDecimal price;
        int copies;
        int available;

        Book(String id, String title, String author, int year, String category,
             BigDecimal price, int copies, int available) {
            this.id = id;
            this.title = title;
            this.author = author;
            this.year = year;
            this.category = category;
            this.price = price;
            this.copies = copies;
            this.available = available;
        }
    }

    static class LibraryModel extends AbstractTableModel {
        private final String[] cols = {
                "id", "title", "author", "year", "price", "category", "copies", "available"
        };
        private List<Book> books = new ArrayList<>();

        public void setBooks(List<Book> books) {
            this.books = new ArrayList<>(Objects.requireNonNull(books));
            fireTableDataChanged();
        }

        public List<Book> getBooks() { return books; }

        public void addBook(Book b) {
            books.add(b);
            int r = books.size() - 1;
            fireTableRowsInserted(r, r);
        }

        public Book getBookAt(int row) {
            return books.get(row);
        }

        @Override public int getRowCount() { return books.size(); }
        @Override public int getColumnCount() { return cols.length; }
        @Override public String getColumnName(int col) { return cols[col]; }

        @Override public Object getValueAt(int rowIndex, int columnIndex) {
            Book b = books.get(rowIndex);
            switch (columnIndex) {
                case 0: return b.id;
                case 1: return b.title;
                case 2: return b.author;
                case 3: return b.year;
                case 4: return b.price;
                case 5: return b.category;
                case 6: return b.copies;
                case 7: return b.available;
                default: return "";
            }
        }

        @Override public boolean isCellEditable(int row, int col) {
            return col == 4;
        }

        @Override public void setValueAt(Object aValue, int row, int col) {
            if (col == 4) {
                try {
                    BigDecimal p = new BigDecimal(aValue.toString().trim());
                    if (p.compareTo(BigDecimal.ZERO) >= 0) {
                        books.get(row).price = p;
                        fireTableRowsUpdated(row, row);
                    }
                } catch (Exception ignored) { }
            }
        }
    }
}
