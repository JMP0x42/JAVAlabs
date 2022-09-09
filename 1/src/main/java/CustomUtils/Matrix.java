package CustomUtils;



public class Matrix {
	private int cols, rows;
	private double[][] matrix;

	public Matrix(int matrixRows, int matrixCols) throws NegativeArraySizeException {
		if (matrixCols <= 0 || matrixRows <= 0) {
			throw new NegativeArraySizeException("Размер матрицы не может быть отрицательным");
		}

		cols = matrixCols;
		rows = matrixRows;
		matrix = new double[rows][cols];
	}


	public Matrix(double[][] m) throws IllegalArgumentException {
		if (m == null) {
			throw new NullPointerException("Матрица не может быть нулевой");
		}

		rows = m.length;
		cols = m[0].length;
		matrix = m;
	}


	public Matrix(Matrix another) {
		matrix = new double[another.rows][another.cols];
		rows = another.rows;
		cols = another.cols;

		for (var i = 0; i < rows; i++) {
			System.arraycopy(matrix[i], 0, another.matrix[i], 0, cols);
		}
	}


	public String toString() {
		StringBuilder result = new StringBuilder();
		for (var i = 0; i < rows; i++) {
			for (var j = 0; j < cols; j++) {
				result.append(matrix[i][j]);
				result.append(" ");
			}
			result.append("\n");
		}

		return result.toString();
	}

	public int getRows() {
		return rows;
	}


	public int getCols() {
		return cols;
	}


	public double get(int row, int column) throws IndexOutOfBoundsException {
		if (row >= rows || column >= cols) {
			throw new IndexOutOfBoundsException("Индекс вне пределов");
		}

		return matrix[row][column];
	}


	public Matrix add(Matrix another) throws UnsupportedOperationException {
		if (cols != another.cols || rows != another.rows) {
			throw new UnsupportedOperationException("Матрицы должны иметь равные размеры для суммирования");
		}

		var result = new Matrix(cols, rows);

		for (int i = 0; i < cols; i++) {
			for (int j = 0; j < rows; j++) {
				result.matrix[i][j] = matrix[i][j] + another.matrix[i][j];
			}
		}

		return result;
	}


	public Matrix sub(Matrix another) throws UnsupportedOperationException {
		if (cols != another.cols || rows != another.rows) {
			throw new UnsupportedOperationException("Матрицы должны иметь равные размеры, чтобы вычесть его");
		}

		var result = new Matrix(cols, rows);

		for (int i = 0; i < cols; i++) {
			for (int j = 0; j < rows; j++) {
				result.matrix[i][j] = matrix[i][j] - another.matrix[i][j];
			}
		}

		return result;
	}

	public Matrix mult(Matrix another) throws UnsupportedOperationException {
		if (cols != another.rows) {
			throw new UnsupportedOperationException("Матрицы неправильного размера при умножении\n");
		}
		
		var result = new Matrix(cols, another.rows);

		for (var i = 0; i < result.cols; i++) {
			for (var j = 0; j < result.rows; j++) {
				double value = 0;
				for (var k = 0; k < cols; k++) {
					value += matrix[i][k] * another.matrix[k][j];
				}
				result.matrix[i][j] = value;
			}
		}

		return result;
	}

	public Matrix mult(int multiplier) {
		var result = new Matrix(rows, cols);

		for (var i = 0; i < result.rows; i++) {
			for (var j = 0; j < result.cols; j++) {
				result.matrix[i][j] = matrix[i][j] * multiplier;
			}
		}

		return result;
	}


	public Matrix mult(double multiplier) {
		var result = new Matrix(rows, cols);

		for (var i = 0; i < result.rows; i++) {
			for (var j = 0; j < result.cols; j++) {
				result.matrix[i][j] = matrix[i][j] * multiplier;
			}
		}

		return result;
	}


	public void set(double value, int col, int row) throws IndexOutOfBoundsException {
		if (col >= cols || row >= rows) {
			throw new IndexOutOfBoundsException("Недопустимый индекс в заданной операции");
		}

		matrix[col][row] = value;
	}

	public void transpose() {
		var result = new double[cols][rows];

		for (var i = 0; i < rows; i++) {
			for (var j = 0; j < cols; j++) {
				result[j][i] = matrix[i][j];
			}
		}

		rows = result.length;
		cols = result[0].length;
		matrix = result;
	}



	public static Matrix getIdentityMatrix(int size) {
		var result = new Matrix(size,size);

		for (var i = 0; i < size; i++) {
			result.matrix[i][i] = 1;
		}

		return result;
	}

	public Matrix reverse() throws UnsupportedOperationException {
		if (getDeter() == 0) {
			throw new UnsupportedOperationException("\n" +
					"Определение матрицы = 0");
		}

		var copy = new Matrix(rows, cols);

		for (var i = 0; i < rows; i++) {
			if (cols >= 0) System.arraycopy(matrix[i], 0, copy.matrix[i], 0, cols);
		}

		var iden = getIdentityMatrix(cols);

		for (var i = 0; i < cols; i++) {
			var mult = copy.matrix[i][i];

			for (var j = 0; j < cols; j++) { // Делим строку на множитель
				copy.matrix[i][j] /= mult;
				iden.matrix[i][j] /= mult;
			}
			for (var j = 0; j < rows; j++) {
				if (j != i) {
					var localmult = copy.matrix[j][i];
					for (var k = 0; k < cols; k++) {
						copy.matrix[j][k] -= localmult * copy.matrix[i][k];
						iden.matrix[j][k] -= localmult * iden.matrix[i][k];
					}
				}
			}
		}

		return iden;
	}



	public double getDeter() {
		double result = 0;
		if (rows == 3 && cols == 3) {
			result = matrix[0][0] * matrix[1][1] * matrix[2][2] + matrix[2][0] * matrix[0][1] * matrix[1][2] +
				matrix[0][2] * matrix[1][0] * matrix[2][1] - matrix[2][0] * matrix[1][1] * matrix[0][2] -
				matrix[1][0] * matrix[0][1] * matrix[2][2] - matrix[2][1] * matrix[1][2] * matrix[0][0];
			return result;
		}

		if (rows == 2 && cols == 2) {
			result = matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
			return result;
		}

		for (var i = 0; i < cols; i++) {
			var j = 0;
			var tmp = new Matrix(rows - 1, cols - 1);
			while (j < i) {
				var k = 1;
				while (k < rows) {
					tmp.matrix[k - 1][j] = matrix[k][j];
					k++;
				}
				j++;
			}

			j = i + 1;

			while (j < cols) {
				var k = 1;
				while (k < rows) {
					tmp.matrix[k - 1][j - 1] = matrix[k][j];
					k++;
				}
				j++;
			}

			if (i % 2 == 0) {
				result += matrix[0][i] * tmp.getDeter();
			} else {
				result -= matrix[0][i] * tmp.getDeter();
			}
		}

		return result;
	}


	public double getAlgebraicComplement(int row, int column) {
		double result = 0;

		var tmp = new Matrix(rows - 1, cols - 1);

		var i = 0;

		while (i < row) {
			var j = 0;
			while (j < column) {
				tmp.matrix[i][j] = matrix[i][j];
				j++;
			}

			j = column + 1;

			while (j < cols) {
				tmp.matrix[i][j - 1] = matrix[i][j];
				j++;
			}
			i++;
		}

		i = row + 1;

		while (i < rows) {
			var j = 0;
			while (j < column) {
				tmp.matrix[i - 1][j] = matrix[i][j];
				j++;
			}

			j = column + 1;

			while (j < cols) {
				tmp.matrix[i - 1][j - 1] = matrix[i][j];
				j++;
			}
			i++;
		}

		result = tmp.getDeter();

		if ((row + column) % 2 != 0) {
			result = -result;
		}

		return result;
	}


	public double[][] getRawMatrix() {
		var result = new double[rows][cols];

		for (var i = 0; i < rows; i++) {
			System.arraycopy(matrix[i], 0, result[i], 0, cols);
		}

		return result;
	}


	public Matrix getAlliedMatrix() {
		var result = new Matrix(rows, cols);

		for (var i = 0; i < rows; i++) {
			for (var j = 0; j < cols; j++) {
				result.matrix[i][j] = getAlgebraicComplement(i, j);
			}
		}

		return result;
	}
}
