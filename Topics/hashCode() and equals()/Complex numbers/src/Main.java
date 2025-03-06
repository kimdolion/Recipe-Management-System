import java.util.Objects;

class ComplexNumber {

    private final double re;
    private final double im;

    public ComplexNumber(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public double getRe() {
        return re;
    }

    public double getIm() {
        return im;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Reflexivity: an object must be equal to itself
        if (obj == null || getClass() != obj.getClass()) return false; // Ensure same class type

        ComplexNumber that = (ComplexNumber) obj;

        return Double.compare(this.re, that.re) == 0 &&
                Double.compare(this.im, that.im) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(re, im); // Uses Double.hashCode internally
    }
}