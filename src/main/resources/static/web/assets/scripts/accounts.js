const { createApp } = Vue
import {logout} from './logout.js'
createApp({
    data() {
        return {
            clients: [],
            accounts: [],
            loans:[],
            client:[],
             
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            this.client = JSON.parse(localStorage.getItem('client'))??[]
            axios.get(`http://localhost:8080/api/clients/current/accounts`)
                .then(response => {
                    const accounts= response.data
                    this.accounts = accounts.filter(account => account.status == true);
                    console.log(this.accounts);
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
				confirmButtonText: 'Yes',
				showLoaderOnConfirm: true,
                buttonColor: '#32a852',
				preConfirm: login => {
					return axios
						.post('/api/clients/current/accounts')
						.then(response => {
                            setTimeout(() =>{
							location.reload()
                            },2000)
						})
						.catch(error => {
							Swal.fire({
								icon: 'error',
								text: error.response.data,
								confirmButtonColor: '#5b31be93',
							});
                            setTimeout(() => {
                                location.reload()
                            },2000)
						});
				},
				allowOutsideClick: () => !Swal.isLoading(),
			});
        },
    }
}).mount("#app")