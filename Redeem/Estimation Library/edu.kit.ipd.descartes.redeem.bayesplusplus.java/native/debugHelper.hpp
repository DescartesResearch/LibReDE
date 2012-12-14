#define TRACE
#define ASSERT

void assert_transformed_vector(FM::Vec vec, double* arr) {
	for (unsigned int i = 0; i < vec.size(); i++)
	{
		if (vec[i] != arr[i]) {
			std::cerr << "assert_transformed_vector: FAILED" << std::endl;
			exit(1);
		}
	}
}

template<class T>
void assert_transformed_matrix(T mat, double* arr) {
	for (unsigned int i = 0; i < mat.size1(); i++)
	{
		for (unsigned int j = 0; j < mat.size2(); j++)
		{

			if (mat(i, j) != arr[i * mat.size2() + j])
			{
				std::cerr << "assert_transformed_matrix: FAILED" << std::endl;
				exit(1);
			}
		}
	}
}

template<class T>
void print_vector(T vector, std::size_t size)
{
	std::cout << "[";
	for (unsigned int i = 0; i < size; i++)
	{
		if (i > 0) {
			std::cout << " ";
		}
		std::cout << vector[i];
	}
	std::cout << "]";
}

template<class T>
double get_value_in_matrix(T matrix, unsigned int i, unsigned int j, std::size_t row_offset)
{
	return matrix(i, j);
}

template<>
double get_value_in_matrix<double*>(double* matrix, unsigned int i, unsigned int j, std::size_t row_offset)
{
	return matrix[i * row_offset + j];
}

template<class T>
void print_matrix(T matrix, std::size_t size1, std::size_t size2)
{
	std::cout << "[";
	for (unsigned int i = 0; i < size1; i++)
	{
		if (i > 0)
		{
			std::cout << "; ";
		}
		for (unsigned int j = 0; j < size2; j++)
		{
			if (j > 0)
			{
				std::cout << " ";
			}
			std::cout << get_value_in_matrix(matrix, i, j, size2);
		}
	}
	std::cout << "]";
}
