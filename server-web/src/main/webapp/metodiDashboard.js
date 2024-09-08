var userId = sessionStorage.getItem("userId");

document.addEventListener("DOMContentLoaded", function () {
  const pageReloaded = sessionStorage.getItem("pageReloaded");

  if (!userId) {
    // Se userId non è presente in sessionStorage, lo carichiamo da localStorage
    userId = localStorage.getItem("userId");

    if (userId) {
      // Salviamo userId in sessionStorage per mantenerlo specifico a questa scheda
      sessionStorage.setItem("userId", userId);
    }
  }

  if (pageReloaded) {
    // L'utente ha ricaricato la pagina, ma userId rimane invariato
    sessionStorage.removeItem("pageReloaded");

    // Continua con il comportamento normale della pagina
    if (userId) {
      document.getElementById("userIdDisplay").textContent = `ID Utente: ${userId}`;
      openTab(null, "domini");
    } else {
      alert("Errore: ID utente non trovato. Effettua nuovamente l'accesso.");
      window.location.href = "index.html";
    }
  } else {
    // Prima volta che la pagina viene caricata in questa sessione
    sessionStorage.setItem("pageReloaded", "true");

    // Continua con il comportamento normale della pagina
    if (userId) {
      document.getElementById("userIdDisplay").textContent = `ID Utente: ${userId}`;
      openTab(null, "domini");
    } else {
      alert("Errore: ID utente non trovato. Effettua nuovamente l'accesso.");
      window.location.href = "index.html";
    }
  }
});

function openTab(evt, tabName) {
  if (page) {
    showModal(tabName, evt);
  } else {
    switchTab(evt, tabName);
  }
}

function switchTab(evt, tabName) {
  var i, tabcontent, tablinks;
  tabcontent = document.getElementsByClassName("tabcontent");

  // Nascondi tutti i tab
  for (i = 0; i < tabcontent.length; i++) {
    tabcontent[i].style.display = "none";
  }

  // Rimuovi la classe 'active' da tutti i bottoni dei tab
  tablinks = document.getElementsByClassName("tablink");
  for (i = 0; i < tablinks.length; i++) {
    tablinks[i].className = tablinks[i].className.replace(" active", "");
  }

  // Mostra il tab selezionato e aggiungi la classe 'active' al bottone cliccato
  document.getElementById(tabName).style.display = "block";
  if (evt != null && evt.currentTarget != null) {
    evt.currentTarget.className += " active";
  }


  if (active) {
    stopCountdown();
  }

  if (tabName === "domini") {
    loadDomini(userId);
  } else if (tabName === "ordini") {
    loadOrdini(userId);
  } else if (tabName === "rinnovoDominio") {
    resetRinnovoForm();
  } else {
    stopCountdown();
    resetPurchaseForm();
    showBuyForm();
  }
}

function showModal(tabName, evt) {
  document.getElementById("modalOverlay").classList.add("show");
  document.getElementById("confirmBtn").addEventListener("click", function () {
    stopCountdown();
    closeModal();
    resetPurchaseForm();
    showBuyForm();
    console.log(evt);
    console.log(tabName);
    switchTab(evt, tabName);
  });

  document.getElementById("cancelBtn").addEventListener("click", function () {
    closeModal();
    return;
  });
}

function closeModal() {
  document.getElementById("modalOverlay").classList.remove("show");
}

// Variabile per il timer
let countdownInterval;
let active = false;

// Funzione per avviare il timer
function startCountdown() {
  clearInterval(countdownInterval);
  document.getElementById("countdownTimer").style.display = "block"; // Mostra il timer
  document.querySelector(".container").style.marginTop = "50px"; // Riduci il margine superiore
  let timer = 120; // 2 minuti in secondi
  active = true;

  countdownInterval = setInterval(() => {
    let minutes = Math.floor(timer / 60);
    let seconds = timer % 60;
    document.getElementById("minutes").textContent =
      minutes < 10 ? "0" + minutes : minutes;
    document.getElementById("seconds").textContent =
      seconds < 10 ? "0" + seconds : seconds;

    if (timer > 0) {
      timer--;
    } else {
      clearInterval(countdownInterval);
      alert("Countdown scaduto, impossibile proseguire all'acquisto.");
      document.getElementById("countdownTimer").style.display = "none";
      document.getElementById("minutes").textContent = "02";
      document.getElementById("seconds").textContent = "00";
      openTab(null, "acquista"); // Torna alla schermata di acquisto del dominio
    }
  }, 1000);
}

// Funzione per fermare il countdown
function stopCountdown() {
  active = false;
  clearInterval(countdownInterval);
  document.getElementById("countdownTimer").style.display = "none";
  document.getElementById("minutes").textContent = "02";
  document.getElementById("seconds").textContent = "00";
}

function updateRinnovoPrice() {
  const durataRinnovo = parseInt(
    document.getElementById("durataRinnovo").value
  );
  const basePrice = 10; // Prezzo base per anno, puoi modificare questo valore in base alla tua logica

  const prezzoRinnovo = durataRinnovo * basePrice;
  document.querySelector("#prezzoRinnovo .price-value").textContent =
    prezzoRinnovo.toFixed(2);
}

// Funzione fittizia per ottenere il prezzo del rinnovo
function getRinnovoPrice(durata) {
  const prezzoPerAnno = 10; // Prezzo per anno, da aggiornare in base alla logica aziendale
  return durata * prezzoPerAnno;
}

// Reset del form di rinnovo
function resetRinnovoForm() {
  document.getElementById("rinnovoForm").reset();
  document.querySelector("#prezzoRinnovo .price-value").textContent = "10";
}

// Funzione per caricare i domini registrati
async function loadDomini(userId) {
  try {
    const response = await fetch(
      `http://localhost:8080/api/domains/users/${userId}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      }
    );

    if (response.ok) {
      const domains = await response.json();
      console.log("Domains:", domains); // Log dei dati ricevuti
      const domainsTableBody = document.getElementById("dominiTableBody");
      domainsTableBody.innerHTML = ""; // Clear any existing content
      domains.forEach((domain) => {
        // Assicurati che i campi siano presenti e validi
        const nome = domain.nome + "." + domain.tld || "N/A";
        const dataRegistrazione = domain.dataRegistrazione
          ? new Date(domain.dataRegistrazione).toLocaleDateString()
          : "N/A";
        const dataScadenza = domain.dataScadenza
          ? new Date(domain.dataScadenza).toLocaleDateString()
          : "N/A";
        const azione = isRenewalAvailable(domain.dataScadenza)
          ? `<button onclick="renewDomain('${domain.id}', '${domain.nome}', '${domain.tld}', '${domain.dataScadenza}')">Rinnova</button>`
          : `<button disabled style="background-color: red; color: white;">Non disponibile</button>`;

        const row = document.createElement("tr");
        row.innerHTML = `
                    <td>${nome}</td>
                    <td>${dataRegistrazione}</td>
                    <td>${dataScadenza}</td>
                    <td>${azione}</td>
                `;
        domainsTableBody.appendChild(row);
      });
    } else {
      const errorData = await response.json();
      console.error("Error response:", errorData); // Log del messaggio di errore
      alert(`Errore: ${errorData.message}`);
    }
  } catch (error) {
    console.error("Error fetching domains:", error); // Log dell'errore di connessione
    alert("Errore di connessione al server.");
  }
}

async function completePurchaseDominio(event) {
  event.preventDefault(); // Previeni il comportamento predefinito del form
  const idDominio = await postDom();
  console.log(idDominio);
  const prezzoAcquisto = parseFloat(
    document.querySelector(".price-value").textContent
  );
  console.log(prezzoAcquisto);
  postOrd(idDominio, "registrazione", prezzoAcquisto);
  page = false;
  openTab(null, "domini"); // Aggiorna la tab dei domini
}

async function postDom() {
  const nome = document.getElementById("nomeDominio").value;
  const temptld = document.getElementById("tld").value;
  const durata = document.getElementById("durata").value;
  // Tolgo il punto (dot) dal TLD
  const tld = temptld.slice(1);
  const proprietario = userId;
  const today = new Date();
  const year = today.getFullYear();
  const month = String(today.getMonth() + 1).padStart(2, "0");
  const day = String(today.getDate()).padStart(2, "0");
  const dataRegistrazione = `${year}-${month}-${day}`;
  const newYear = year + parseInt(durata, 10);
  const dataScadenza = `${newYear}-${month}-${day}`;

  try {
    // Prima post per inserire il nuovo dominio
    const responseDomain = await fetch("http://localhost:8080/api/domains", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        idDominio: 0, // Lascia che il server generi l'ID
        nome,
        tld,
        dataRegistrazione,
        dataScadenza,
        stato: "attivo",
        proprietario,
      }),
    });

    if (!responseDomain.ok) {
      throw new Error("Errore durante l'acquisto del dominio");
    }

    // Verifica se l'header Location è presente
    const locationHeader = responseDomain.headers.get("Location");
    if (!locationHeader) {
      throw new Error("Header Location non presente nella risposta");
    }

    const responseNewIdDomain = locationHeader.split("/").pop();
    console.log(responseNewIdDomain);
    return responseNewIdDomain;
  } catch (error) {
    console.error("Errore durante l'acquisto del dominio:", error);
    alert("Errore di connessione al server.");
  }
}

async function postOrd(idDominio, oggetto, importoPagato) {
  console.log("idDominio: creato con successo:", idDominio);
  const idUtente = userId;
  const idOrdine = 0; // Questo campo sarà probabilmente ignorato dal server o auto-generato

  const today = new Date();
  const year = today.getFullYear();
  const month = String(today.getMonth() + 1).padStart(2, "0");
  const day = String(today.getDate()).padStart(2, "0");
  const dataOrdine = `${year}-${month}-${day}`; // Correggi la sintassi per creare una stringa

  try {
    // Seconda post per l'ordine
    const responseOrder = await fetch("http://localhost:8080/api/orders", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        idOrdine,
        idUtente,
        idDominio,
        dataOrdine,
        oggetto,
        importoPagato,
      }),
    });

    if (!responseOrder.ok) {
      throw new Error("Errore durante l'aggiunta dell'ordine");
    }

    // Verifica se l'header Location è presente
    const locationHeader = responseOrder.headers.get("Location");
    if (!locationHeader) {
      throw new Error("Header Location non presente nella risposta");
    }

    const responseNewIdDomain = locationHeader.split("/").pop();
    console.log("Ordine creato con successo:", responseNewIdDomain);
  } catch (error) {
    console.error("Errore durante la creazione dell'ordine:", error); // Log dell'errore di connessione
    alert("Errore di connessione al server.");
  }
}

// Funzione per acquistare un nuovo dominio
async function purchaseDominio(event) {
  console.log("attivato ora");
  event.preventDefault(); // Previeni il comportamento predefinito del form
  const nomeDominio = document.getElementById("nomeDominio").value;
  const temptld = document.getElementById("tld").value;
  const durata = document.getElementById("durata").value;
  const tld = temptld.slice(1);

  // Controlla se il dominio esiste già
  try {
    const checkResponse = await fetch(
      `http://localhost:8080/api/domains/${nomeDominio}/${tld}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      }
    );

    if (checkResponse.ok) {
      const responseText = await checkResponse.text();
      const existingDomain = responseText ? JSON.parse(responseText) : null;

      if (existingDomain != null && Object.keys(existingDomain).length > 0) {
        // Il dominio esiste, mostra i dettagli del proprietario
        const getProprietario = await fetch(
          `http://localhost:8080/api/users/${existingDomain.proprietario}`,
          {
            method: "GET",
            headers: {
              "Content-Type": "application/json",
            },
          }
        );

        if (getProprietario.ok) {
          const existingProprietario = await getProprietario.json();

          alert(`Dominio già esistente!
                        Nome proprietario: ${existingProprietario.nome}
                        Cognome proprietario: ${existingProprietario.cognome}
                        Email proprietario: ${existingProprietario.email}
                        Data scadenza: ${existingDomain.dataScadenza}`);
          return; // Esci dalla funzione
        }
      } else {
        console.log("Dominio non esistente, procedere con l'acquisto.");
        showPaymentForm();
      }
    } else if (checkResponse.status === 409) {
      // Gestione di altri possibili errori di risposta
      const errorData = await checkResponse.json();
      console.error("Error response:", errorData);
      alert(`Errore: ${errorData.message}`);
      return;
    }
  } catch (error) {
    console.error("Errore nel controllo del dominio:", error); // Log dell'errore di connessione
    alert("Errore di connessione al server.");
    return;
  }
}

async function completePurchaseDomain() {
  var v = postDom();
  const prezzoAcquisto = parseFloat(
    document.querySelector(".price-value").textContent
  );
  postOrd(v, "registrazione", prezzoAcquisto);
  openTab(null, "domini");
}

// Funzione per verificare se è disponibile il rinnovo
function isRenewalAvailable(dataScadenza) {
  const currentDate = new Date();
  const expirationDate = new Date(dataScadenza);
  const tenYearsFromNow = new Date();
  tenYearsFromNow.setFullYear(currentDate.getFullYear() + 10);
  return expirationDate <= tenYearsFromNow;
}

// Funzione per calcolare il numero massimo di anni per il rinnovo
function calcolaAnniMassimi(dataScadenza, dataOdierna) {
  // Converti le date in oggetti Date
  const scadenza = new Date(dataScadenza);
  const odierna = new Date(dataOdierna);

  // Calcola la differenza in anni tra la data di scadenza e la data odierna
  const differenzaAnni = scadenza.getFullYear() - odierna.getFullYear();
  const anniDisponibili = 10 - differenzaAnni;

  // Se il risultato è negativo o zero, imposta a 1 (minimo consentito)
  return anniDisponibili > 0 ? anniDisponibili : 1;
}

async function renewDomain(domainId, domainName, tld, currentDataScadenza) {
  // Ottieni la data odierna
  const dataOdierna = new Date().toISOString().split("T")[0];

  // Calcola gli anni massimi di rinnovo
  const maxAnniRinnovo = calcolaAnniMassimi(currentDataScadenza, dataOdierna);

  // Apri la tab del rinnovo e imposta i campi del form
  openTab(null, "rinnovoDominio");
  document.getElementById("dominioRinnovo").value = `${domainName}.${tld}`;

  // Imposta il valore massimo per la durata del rinnovo
  const durataRinnovoInput = document.getElementById("durataRinnovo");
  durataRinnovoInput.max = maxAnniRinnovo;
  durataRinnovoInput.value = 1;

  // Gestisci l'invio del form per il rinnovo
  const rinnovoForm = document.getElementById("rinnovoForm");
  rinnovoForm.onsubmit = async function (event) {
    event.preventDefault(); // Previeni il comportamento predefinito del form
    const durataRinnovo = parseInt(durataRinnovoInput.value, 10);

    try {
      // Fetch il dominio corrente
      const domainResponse = await fetch(
        `http://localhost:8080/api/domains/${domainId}`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      if (!domainResponse.ok) {
        const errorData = await domainResponse.json();
        throw new Error(`Errore: ${errorData.message}`);
      }

      const domain = await domainResponse.json();

      // Calcola la nuova data di scadenza
      const currentDataScadenzaDate = new Date(currentDataScadenza);
      const nuovaDataScadenza = new Date(
        currentDataScadenzaDate.setFullYear(
          currentDataScadenzaDate.getFullYear() + durataRinnovo
        )
      );

      // Aggiorna la data di scadenza nel dominio
      domain.dataScadenza = nuovaDataScadenza.toISOString().split("T")[0];

      // Invia il dominio aggiornato con la nuova data di scadenza
      const response = await fetch(`http://localhost:8080/api/domains`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(domain),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(`Errore: ${errorData.message}`);
      }

      const price = getRinnovoPrice(durataRinnovo);

      // Creazione dell'oggetto ordine per il rinnovo
      const ordine = {
        idUtente: userId,
        idDominio: domainId,
        dataOrdine: dataOdierna,
        oggetto: "rinnovo",
        importoPagato: price, // Chiamata alla funzione per ottenere il prezzo del rinnovo
      };

      const prezzoRinnovo = parseFloat(
        document.querySelector(".price-value").textContent
      );
      // Effettua una richiesta POST per creare l'ordine di rinnovo
      postOrd(domainId, "rinnovo", price);

      alert("Ordine di rinnovo creato con successo!");
      openTab(null, "domini");
    } catch (error) {
      console.error("Errore durante il rinnovo del dominio:", error);
      alert("Errore di connessione al server.");
    }
  };
}

// Funzione per caricare gli ordini
async function loadOrdini(userId) {
  console.log(`Fetching orders for userId: ${userId}`);
  try {
    // Prima chiamata API per ottenere gli ordini
    const ordersResponse = await fetch(
      `http://localhost:8080/api/orders/${userId}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      }
    );

    if (!ordersResponse.ok) {
      const errorData = await ordersResponse.json();
      console.error("Error response:", errorData);
      alert(`Errore: ${errorData.message}`);
      return;
    }

    const ordini = await ordersResponse.json();
    console.log("Ordini:", ordini);

    // Funzione per ottenere i dati del dominio
    async function fetchDominio(idDominio) {
      const response = await fetch(
        `http://localhost:8080/api/domains/${idDominio}`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (!response.ok) {
        const errorData = await response.json();
        console.error("Error fetching domain:", errorData);
        throw new Error(`Errore: ${errorData.message}`);
      }
      return response.json();
    }

    // Array di Promises per ottenere i domini
    const domainPromises = ordini.map((order) => fetchDominio(order.idDominio));

    // Risolvere tutte le Promises per ottenere i domini
    const domini = await Promise.all(domainPromises);
    console.log("Domini:", domini);

    // Unire gli ordini e i domini
    const ordiniTableBody = document.getElementById("ordiniTableBody");
    ordiniTableBody.innerHTML = ""; // Clear any existing content

    ordini.forEach((order) => {
      const dominio = domini.find((d) => d.id === order.idDominio);
      const nomeDominio = dominio ? `${dominio.nome}.${dominio.tld}` : "N/A";
      const dataOrdine = order.dataOrdine
        ? new Date(order.dataOrdine).toLocaleDateString()
        : "N/A";
      const oggetto = order.oggetto || "N/A";
      const quotaPagata = order.importoPagato
        ? `€${order.importoPagato}`
        : "N/A";

      const row = document.createElement("tr");
      row.innerHTML = `
                <td>${nomeDominio}</td>
                <td>${dataOrdine}</td>
                <td>${oggetto}</td>
                <td>${quotaPagata}</td>
            `;
      ordiniTableBody.appendChild(row);
    });
  } catch (error) {
    console.error("Error fetching orders or domains:", error);
    alert("Errore di connessione al server.");
  }
}

function rinnovaOrdine() {
  const ordineId = document.getElementById("ordineId").value;
  const durataRinnovo = parseInt(
    document.getElementById("durataRinnovo").value
  );

  const ordine = {
    id: ordineId,
    durataRinnovo: durataRinnovo,
    oggetto: "rinnovo",
  };

  fetch("http://localhost:8080/api/orders", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(ordine),
  })
    .then(function (response) {
      if (response.ok) {
        // Accedi all'header Location
        var location = response.headers.get("Location");
        if (location) {
          alert(
            "Rinnovo effettuato con successo! Nuovo ordine creato: " + location
          );
          // Puoi aggiornare la UI con l'URI della nuova risorsa, se necessario
        } else {
          alert(
            "Rinnovo effettuato con successo, ma l'header Location non è presente."
          );
        }
      } else {
        // Se la risposta non è OK, prova a parsare il corpo come JSON
        return response
          .json()
          .then(function (json) {
            throw new Error("Errore nel server: " + json.error);
          })
          .catch(function () {
            // In caso di fallimento nel parsing, fall back su text()
            return response.text().then(function (text) {
              throw new Error("Errore nel server: " + text);
            });
          });
      }
    })
    .catch(function (error) {
      // Gestione dell'errore
      console.error("Si è verificato un errore:", error);
      alert("Si è verificato un errore: " + error.message);
    });
}

// Funzione per mostrare il form di acquisto dominio
function openPurchaseForm() {
  resetPurchaseForm();
  openTab(null, "acquista");
}

// Funzione per resettare il form di acquisto
function resetPurchaseForm() {
  document.getElementById("purchaseForm").reset();
  document.querySelector("#acquistaFormContainer .price-value").textContent =
    "0";
  document.getElementById("acquistaFormContainer").style.display = "block";
  document.getElementById("payment-section").style.display = "none";
}

// Funzione per completare l'acquisto e tornare alla situazione iniziale
function completePurchase(event) {
  event.preventDefault();
  // Codice per completare l'acquisto
  alert("Pagamento completato!");
  openPurchaseForm();
}

// Imposta la data minima per la scadenza della carta
document
  .getElementById("scadenzaCarta")
  .setAttribute("min", new Date().toISOString().split("T")[0].slice(0, 7));

// Aggiungi la formattazione automatica al campo del numero di carta
document.getElementById("numeroCarta").addEventListener("input", function (e) {
  var value = e.target.value.replace(/\s+/g, "");
  if (value.length > 16) {
    value = value.substr(0, 16);
  }
  var formattedValue = "";
  for (var i = 0; i < value.length; i += 4) {
    if (i > 0) {
      formattedValue += " ";
    }
    formattedValue += value.substr(i, 4);
  }
  e.target.value = formattedValue;
});

document
  .getElementById("purchaseForm")
  .addEventListener("submit", purchaseDominio);

// Aggiungi l'event listener per calcolare il prezzo
document.getElementById("durata").addEventListener("input", function (e) {
  var durata = e.target.value;
  var prezzo = durata * 10;
  document.querySelector(".price-value").textContent = `${prezzo}`;
});

// Mostra il form di pagamento e nascondi il form di acquisto e il titolo

//true se bisogna mostrare il timer
var page = false;

function showPaymentForm() {
  var prezzo = document.querySelector(".price-value").textContent;
  document.getElementById("purchaseForm").style.display = "none";
  document.getElementById("acquistaTitle").style.display = "none";
  document.getElementById("payment-section").style.display = "block";
  document.querySelector("#payment-section .price-value").textContent = prezzo;
  startCountdown();
  page = true;
  console.log(page); //debug
}

function showBuyForm() {
  document.getElementById("purchaseForm").style.display = "block";
  document.getElementById("acquistaTitle").style.display = "block";
  document.getElementById("payment-section").style.display = "none";
  page = false;
  console.log(page); //debug
}

function closeModal() {
  document.getElementById("modalOverlay").classList.remove("show");
}
