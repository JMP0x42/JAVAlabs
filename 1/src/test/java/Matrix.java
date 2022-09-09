import java.util.Arrays;
package test.java;
public class Matrix {
    @Test
    public void getRows() {
        final var rows = 3;
        var matrix = new CustomUtils.Matrix(rows, 4);
        Assert.assertEquals(rows, matrix.getRows(), 0);
    }
    @Test
    public void getCols() {
        final var cols = 3;
        var matrix = new CustomUtils.Matrix(4, cols);

        Assert.assertThrows()
        Assert.assertEquals(cols, matrix.getCols(), 0);
    }
    @Test
    public void sub() {
        final var cols = 2;
        final var rows = 2;

        double[][] data1 = {{1.0, 3.0}, {3.4, 3.6}};
        double[][] data2 = {{5.5, 3.1}, {1.1, 0.5}};
        var result = new double[rows][cols];

        for (var i = 0; i < rows; i++) {
            for (var j = 0; j < cols; j++) {
                result[i][j] = data1[i][j] - data2[i][j];
            }
        }
        var m1 = new Matrix(data1);
        var m2 = new Matrix(data2);
        var m3 = m1.sub(m2);
        Assert.assertTrue(Arrays.deepEquals(result, m3.getRawMatrix()));
    }
    @Test
    public void getSet() {
        final var value = 3;
        final var size = 2;
        final var row = 1;
        final var col = 1;
        var matrix = new Matrix(size, size);
        matrix.set(value, col, row);

        Assert.assertEquals(value, matrix.get(row, col), 0);
    }
    @Test
    public void addata() {
        final var cols = 2;
        final var rows = 2;

        double[][] data1 = {{1.0, 3.0}, {3.4, 3.6}};
        double[][] data2 = {{5.5, 3.1}, {1.1, 0.5}};
        var result = new double[rows][cols];

        for (var i = 0; i < rows; i++) {
            for (var j = 0; j < cols; j++) {
                result[i][j] = data1[i][j] + data2[i][j];
            }
        }
        var m1 = new Matrix(data1);
        var m2 = new Matrix(data2);
        var m3 = m1.addata(m2);
        Assert.assertTrue(Arrays.deepEquals(result, m3.getRawMatrix()));
    }
    @Test
    public void mult() {
        final var cols = 2;
        final var rows = 2;
        double[][] data1 = {{1.0, 3.0}, {3.4, 3.6}};
        double[][] data2 = {{5.5, 3.1}, {1.1, 0.5}};
        var result = new double[rows][cols];
        for (var i = 0; i < rows; i++) {
            for (var j = 0; j < cols; j++) {
                double sum = 0;
                for (var k = 0; k < cols; k++) {
                    sum += data1[i][k] * data2[k][j];
                }
                result[i][j] = sum;
            }
        }
        var m1 = new Matrix(data1);
        var m2 = new Matrix(data2);
        var m3 = m1.mult(m2);
        Assert.assertTrue(Arrays.deepEquals(result, m3.getRawMatrix()));
    }
    @Test
    public void multByDouble() {
        final var cols = 2;
        final var rows = 2;
        final var multiplier = 5.7;
        double[][] data1 = {{1.0, 3.0}, {3.4, 3.6}};
        var result = new double[rows][cols];
        for (var i = 0; i < rows; i++) {
            for (var j = 0; j < cols; j++) {
                result[i][j] = data1[i][j] * multiplier;
            }
        }
        var m1 = new Matrix(data1);
        var m3 = m1.mult(multiplier);
        Assert.assertTrue(Arrays.deepEquals(result, m3.getRawMatrix()));
    }
    @Test
    public void multByInt() {
        final var cols = 2;
        final var rows = 2;
        final var multiplier = 5;
        double[][] data1 = {{1.0, 3.0}, {3.4, 3.6}};
        var result = new double[rows][cols];
        for (var i = 0; i < rows; i++) {
            for (var j = 0; j < cols; j++) {
                result[i][j] = data1[i][j] * multiplier;
            }
        }
        var m1 = new Matrix(data1);
        var m3 = m1.mult(multiplier);
        Assert.assertTrue(Arrays.deepEquals(result, m3.getRawMatrix()));
    }
    @Test
    public void transpose() {
        double[][] data1 = {{1.0, 3.0}, {3.4, 3.6}};
        double[][] transposed = {{1.0, 3.4}, {3.0, 3.6}};
        var m = new Matrix(data1);
        m.transpose();
        Assert.assertTrue(Arrays.deepEquals(transposed, m.getRawMatrix()));
    }
    @Test
    public void getIdentityMatrix() {
        double[][] expectedMatrix = {{1, 0}, {0, 1}};
        Assert.assertTrue(Arrays.deepEquals(expectedMatrix, Matrix.getIdentityMatrix(2).getRawMatrix()));
    }
    @Test
    public void reverse() {
        double[][] source = {{3, 4}, {2, 1}};
        double[][] reversed = {{-0.2, 0.8}, {0.4, -0.6}};
        var m = new Matrix(source);
        var actualReversed = m.reverse();
        for (var i = 0; i < 2; i++) {
            for (var j = 0; j < 2; j++) {
                var value = actualReversed.get(i, j);
                actualReversed.set(Math.round(value * 10.0) / 10.0, i, j);
            }
        }
        Assert.assertTrue(Arrays.deepEquals(reversed, actualReversed.getRawMatrix()));
    }
    @Test
    public void getAlgebraicComplement() {
        double[][] source = {{3, 5, 1}, {4, 6, 2}, {3, 4, 1}};
        var m = new Matrix(source);
        var actualAlgebraicComplement = m.getAlgebraicComplement(2, 1);
        var expectedAlgebraicComplement = -2;
        Assert.assertEquals(expectedAlgebraicComplement, actualAlgebraicComplement, 0);
    }
    @Test
    public void getAlliedMatrix() {
        double[][] source = {{3, 5, 1}, {4, 6, 2}, {3, 4, 1}};
        var m = new Matrix(source);
        var actualAlliedMatrix = m.getAlliedMatrix();
        double[][] expectedAlliedMatrix = {{-2, 2, -2}, {-1, 0, 3}, {4, -2, -2}};
        Assert.assertTrue(Arrays.deepEquals(expectedAlliedMatrix, actualAlliedMatrix.getRawMatrix()));
    }
    @Test
    public void getDeter() {
        double[][] source = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        var m = new Matrix(source);
        var actualDeter = Math.round(m.getDeter() * 1000) / 1000.0;
        var expectedDeter = 0;
        Assert.assertEquals(expectedDeter, actualDeter, 0);
    }
}