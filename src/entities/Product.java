package entities;

public class Product implements Comparable<Product>
{
    //FIELDS
    private String name;
    private Double price;

    //CONSTRUCTORS
    public Product(String name, Double price)
    {
        this.name = name;
        this.price = price;
    }

    //PROPERTY METHODS
    public String getName() {return name;}
    public Double getPrice() {return price;}

    public void setName(String value) {name = value;}
    public void setPrice(Double value) {price = value;}

    //OVERRIDE METHODS
    @Override public int hashCode()
    {
        return "entities.Product".hashCode() + (price == null ? 0 : price.hashCode()) + (name == null ? 0 : name.hashCode());
    }

    @Override public boolean equals(Object o)
    {
        if(this == o) return true;
        if(o == null) return false;
        if(this.getClass() != o.getClass()) return false;
        Product other = (Product) o;
        return name.equals(other.name) && price.equals(other.price);
    }

    @Override public int compareTo(Product p)
    {
        return name.compareTo(p.name);
    }

    @Override public String toString()
    {
        return String.format("Product [name = %s, price = %.2f]", name, price);
    }
}
