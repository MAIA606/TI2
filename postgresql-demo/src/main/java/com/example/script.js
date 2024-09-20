const apiUrl = 'http://localhost:4567/movies';

// Função para carregar a lista de filmes
function loadMovies() {
    fetch(apiUrl)
        .then(response => response.json())
        .then(data => {
            const movieList = document.getElementById('movie-list');
            movieList.innerHTML = ''; // Limpa a lista antes de recarregar
            data.forEach(movie => {
                const movieDiv = document.createElement('div');
                movieDiv.className = 'movie';
                movieDiv.innerHTML = `
                    <span class="close-btn" onclick="deleteMovie(${movie.id})">&times;</span>
                    <h3>${movie.title}</h3>
                    <p><strong>Gênero:</strong> ${movie.genre}</p>
                    <p><strong>Diretor:</strong> ${movie.director}</p>
                    <p><strong>Ano de Lançamento:</strong> ${movie.releaseYear}</p>
                    <button onclick="showUpdateForm(${movie.id})">Atualizar</button>
                `;
                movieList.appendChild(movieDiv);
            });
        })
        .catch(error => console.error('Erro ao carregar filmes:', error));
}

// Função para adicionar um novo filme
document.getElementById('add-movie-form').addEventListener('submit', function(e) {
    e.preventDefault();
    const movie = {
        title: document.getElementById('title').value,
        genre: document.getElementById('genre').value,
        director: document.getElementById('director').value,
        releaseYear: parseInt(document.getElementById('releaseYear').value)
    };
    fetch(apiUrl, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(movie)
    })
    .then(response => {
        if (response.ok) {
            alert('Filme adicionado com sucesso!');
            loadMovies();
            document.getElementById('add-movie-form').reset();
        } else {
            alert('Erro ao adicionar o filme.');
        }
    })
    .catch(error => console.error('Erro ao adicionar filme:', error));
});

// Função para deletar um filme
function deleteMovie(id) {
    if (confirm('Tem certeza que deseja deletar este filme?')) {
        fetch(`${apiUrl}/${id}`, {
            method: 'DELETE'
        })
        .then(response => {
            if (response.ok) {
                alert('Filme deletado com sucesso!');
                loadMovies();
            } else {
                alert('Erro ao deletar o filme.');
            }
        })
        .catch(error => console.error('Erro ao deletar filme:', error));
    }
}

// Função para mostrar o formulário de atualização com os dados atuais
function showUpdateForm(id) {
    fetch(`${apiUrl}/${id}`)
        .then(response => response.json())
        .then(movie => {
            document.getElementById('update-id').value = movie.id;
            document.getElementById('update-title').value = movie.title;
            document.getElementById('update-genre').value = movie.genre;
            document.getElementById('update-director').value = movie.director;
            document.getElementById('update-releaseYear').value = movie.releaseYear;

            document.getElementById('update-movie-form').style.display = 'block';
            window.scrollTo(0, document.body.scrollHeight);
        })
        .catch(error => console.error('Erro ao carregar filme:', error));
}

// Função para esconder o formulário de atualização
function hideUpdateForm() {
    document.getElementById('update-movie-form').style.display = 'none';
    document.getElementById('update-movie-form').reset();
}

// Função para atualizar um filme
document.getElementById('update-movie-form').addEventListener('submit', function(e) {
    e.preventDefault();
    const id = document.getElementById('update-id').value;
    const updatedMovie = {
        title: document.getElementById('update-title').value,
        genre: document.getElementById('update-genre').value,
        director: document.getElementById('update-director').value,
        releaseYear: parseInt(document.getElementById('update-releaseYear').value)
    };
    fetch(`${apiUrl}/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(updatedMovie)
    })
    .then(response => {
        if (response.ok) {
            alert('Filme atualizado com sucesso!');
            loadMovies();
            hideUpdateForm();
        } else {
            alert('Erro ao atualizar o filme.');
        }
    })
    .catch(error => console.error('Erro ao atualizar filme:', error));
});

// Carrega a lista de filmes ao carregar a página
document.addEventListener('DOMContentLoaded', loadMovies);