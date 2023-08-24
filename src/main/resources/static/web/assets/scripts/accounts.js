const { createApp } = Vue

createApp({
    data() {
        return {
            clients: [],
            accounts: [],
            loans:[]
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            axios.get(`http://localhost:8080/api/clients/current`)
                .then(response => {
                    console.log(response);
                    this.clients = response.data
                    console.log(this.clients);
                    this.clients_accounts = this.clients.accounts
                    this.loans= this.clients.loans
                    for (const account of this.clients_accounts) {
                        const aux = {
                            id: account.id,
                            number: account.number,
                            creationDate: account.creationDate,
                            balance: account.balance
                        }
                        this.accounts.push(aux)
                    }
                localStorage.setItem('client',JSON.stringify(this.clients))
                })
        },
        logOut(){
            axios.post("/api/logout")
            .then(response =>{
                location.href = "../../index.html"
            })
            .catch(error=> console.log(error.message))
        },
        createAccount(){
            Swal.fire({
				title: 'Quieres crear una nueva cuenta?',
				inputAttributes: {
					autocapitalize: 'off',
				},
				showCancelButton: true,
				confirmButtonText: 'Sure',
				showLoaderOnConfirm: true,
				preConfirm: login => {
					return axios
						.post('/api/clients/current/accounts')
						.then(response => {
							location.href = '../pages/accounts.html';
						})
						.catch(error => {
							Swal.fire({
								icon: 'error',
								text: error.response.data,
								confirmButtonColor: '#5b31be93',
							});
						});
				},
				allowOutsideClick: () => !Swal.isLoading(),
			});
        }
    }
}).mount("#app")