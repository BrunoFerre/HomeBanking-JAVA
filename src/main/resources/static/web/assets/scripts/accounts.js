const { createApp } = Vue
import {logout} from './logout.js'
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
            logout()
        },
        createAccount(){
            Swal.fire({
				title: 'Add a new account?',
				inputAttributes: {
					autocapitalize: 'off',
				},
				showCancelButton: true,
				confirmButtonText: 'Look up',
				showLoaderOnConfirm: true,
				preConfirm: login => {
					return axios
						.post('/api/clients/current/accounts')
						.then(response => {
                            setTimeout(() =>{
							location.href = '../pages/accounts.html';
                            },2000)
						})
						.catch(error => {
							Swal.fire({
								icon: 'error',
								text: error.response.data,
								confirmButtonColor: '#5b31be93',
							});
                            setTimeout(() => {
                            location.href = '../pages/accounts.html';
                            },2000)
						});
				},
				allowOutsideClick: () => !Swal.isLoading(),
			});
        }
    }
}).mount("#app")