package musicstore.model;

/**
 * Represents a music album in the store
 * Extends the abstract class {@link Product} by adding
 * specific information about an album: artist, genre, and year
 *
 * @author Marcos Galan Carrillo
 */
public class Album extends Product {

    // Artist or band name associated with the album
    private String artist;

    // Musical genre of the album (e.g., Rock, Pop, Jazz)
    private String genre;

    // Publication year of the album
    private int year;


    /**
     * Album constructor
     *
     * @param id      unique identifier
     * @param name    album title
     * @param price   price (euros)
     * @param stock   available stock
     * @param artist  artist or band name
     * @param genre   musical genre
     * @param year    publication year
     */
    public Album(int id, String name, double price, int stock,
                 String artist, String genre, int year) {
        super(id, name, price, stock);
        this.artist = artist;
        this.genre = genre;
        this.year = year;
    }


    /**
     * Returns the artist of the album
     * @return artist name
     */
    public String getArtist() { return artist; }


    /**
     * Sets the artist of the album
     * @param artist new artist name
     */
    public void setArtist(String artist) { this.artist = artist; }


    /**
     * Returns the musical genre of the album
     * @return album genre
     */
    public String getGenre() { return genre; }


    /**
     * Sets the musical genre of the album
     * @param genre new album genre
     */
    public void setGenre(String genre) { this.genre = genre; }


    /**
     * Returns the publication year of the album
     * @return album publication year
     */
    public int getYear() { return year; }


    /**
     * Sets the publication year of the album
     * @param year new album publication year
     */
    public void setYear(int year) { this.year = year; }


    /**
     * {@inheritDoc}
     * @return "Album"
     */
    @Override
    public String getCategory() { return "Album"; }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getDetailedDescription() {
        return String.format("Album: %s | Artist: %s | Genre: %s | Year: %d | Price: %.2f€ | Stock: %d",
                getName(), artist, genre, year, getPrice(), getStock());
    }


    /**
     * {@inheritDoc}
     * Format: ALBUM;id;name;price;stock;artist;genre;year
     */
    @Override
    public String toCSV() {
        return String.format("ALBUM;%d;%s;%.2f;%d;%s;%s;%d",
                getId(), getName(), getPrice(), getStock(), artist, genre, year);
    }
}