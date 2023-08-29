const { createApp } = Vue
import { logout } from './logout.js'
const app = createApp({
    data() {
        return {
            clients: [],
            type: '',
            color: '',
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            axios.get(`/api/clients/current/cards`,)
                .then(response => {
                    this.clients = response.data
                }).catch(error => {

                })
        },
        logOut() {
            logout()
        },
        createCard(event) {
            event.preventDefault()
            Swal.fire({
                title: 'Add a new card?',
                inputAttributes: {
                    autocapitalize: 'off',
                },
                showCancelButton: true,
                confirmButtonText: 'Yes',
                showLoaderOnConfirm: true,
                preConfirm: login => {
                    return axios
                        .post('/api/clients/current/cards',`type=${this.type}&color=${this.color}`
                        ,{headers:{'content-type': 'application/x-www-form-urlencoded'}})
                        .then(response => {
                            setTimeout(() =>{
                            location.href = "../pages/cards.html";  
                            },2000)
                        }).catch(error => {
                            console.log(error);
                            Swal.fire({
                                icon: 'error',
                                title: 'Oops...',
                                text: error.response.data,
                                confirmButtonColor: '#5b31be93',
                            })
                            setTimeout(() => {
                            location.href = "../pages/cards.html";   
                            },2000)
                        
                 })
                }
            })
        }
    }
}).mount("#app")