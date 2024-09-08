// Funzione per aprire i tab
function openTab(evt, tabName) {
    var i, tabcontent, tablinks;
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }
    tablinks = document.getElementsByClassName("tablink");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }
    document.getElementById(tabName).style.display = "block";
    if (evt) evt.currentTarget.className += " active";
}

// Funzione per gestire il login (FATTO)
// Funzione per gestire il login (FATTO)
async function handleLogin(event) {
    event.preventDefault();
    const userId = document.getElementById('loginUserId').value;
    try {
        const response = await fetch(`http://localhost:8080/api/users/${userId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            alert('Accesso effettuato con successo!');
            localStorage.setItem('userId', userId); // Memorizza l'ID utente nel localStorage
            window.location.href = 'dashboard.html';
        } else if (response.status === 404) {  // Controllo del codice di stato
            const errorData = await response.json(); // Assumendo che il server risponda in JSON
            alert(`Errore: ${errorData.error || "Errore sconosciuto"}`); // Usa la proprietà 'error'
        } else {
            // Gestione di altri potenziali errori
            const errorText = await response.text();
            alert(`Errore: ${errorText}`);
        }
    } catch (error) {
        console.error('Errore nella richiesta:', error);
        alert('Si è verificato un errore. Riprova più tardi.');
    }
}



// Funzione per gestire la registrazione (FATTO)
async function handleRegistration(event) {
    event.preventDefault();
    const nome = document.getElementById('nome').value;
    const cognome = document.getElementById('cognome').value;
    const email = document.getElementById('email').value;
    const id = 0;

    try {
        const response = await fetch('http://localhost:8080/api/users', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ id, nome, cognome, email })
        });

        if (response.status === 201) {
            const locationHeader = response.headers.get('Location');
            console.log('Location header:', locationHeader); // Log per debug

            const userId = locationHeader.split('/').pop(); // Assuming the user ID is at the end of the URL
            alert(`Registrazione avvenuta con successo! \n Il tuo id è: ${userId}`);
            localStorage.setItem('userId', userId); // Store user ID in localStorage
            window.location.href = 'dashboard.html';
        } else {
            try {
                const errorData = await response.json();
                alert(`Errore: ${errorData.message}`);
            } catch (e) {
                alert(`Errore: Impossibile leggere il messaggio di errore dal server`);
            }
        }
    } catch (error) {
        alert(`Errore di rete: ${error.message}`);
    }
}


// Funzione per caricare i domini registrati (FATTO)
async function loadRegisteredDomains() {
    try {
        const response = await fetch(`http://localhost:8080/api/domains`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            const domains = await response.json();
            const registeredDomainsTableBody = document.getElementById('registeredDomainsTableBody');
            registeredDomainsTableBody.innerHTML = ''; // Pulisci il contenuto esistente

            domains.forEach(domain => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${domain.nome}${domain.tld}</td>
                    <td>${new Date(domain.dataRegistrazione).toLocaleDateString()}</td>
                    <td>${new Date(domain.dataScadenza).toLocaleDateString()}</td>
                `;
                registeredDomainsTableBody.appendChild(row);
            });
        } else {
            const errorData = await response.json();
            alert(`Errore: ${errorData.message}`);
        }
    } catch (error) {
        console.error('Errore nel caricamento dei domini registrati:', error);
        alert('Errore di connessione al server.');
    }
}

// Imposta gli event listener quando il DOM è caricato 
document.addEventListener('DOMContentLoaded', function () {
    openTab(null, 'login');
    loadRegisteredDomains();
    document.getElementById('loginForm').addEventListener('submit', handleLogin);
    document.getElementById('registrationForm').addEventListener('submit', handleRegistration);
});
