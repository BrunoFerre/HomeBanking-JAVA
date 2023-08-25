const { createApp } = Vue
import { logout } from './logout.js'
const app = createApp({
    data() {
        return {
            clients: [],
            type: '',
            color: ''
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
                title: 'Quieres crear una nueva tarjeta?',
                inputAttributes: {
                    autocapitalize: 'off',
                },
                showCancelButton: true,
                confirmButtonText: 'Sure',
                showLoaderOnConfirm: true,
                preConfirm: login => {
                    return axios
                        .post('/api/clients/current/cards',`type=${this.type}&color=${this.color}`,{headers:{'content-type': 'application/x-www-form-urlencoded'}})
                        .then(response => {
                            location.href = "../pages/cards.html";
                        })
                        .catch(error => {
                            Swal.fire({
                                icon: 'error',
                                confirmButtonColor: '#5b31be93',
                            });
                        })
                }
            })
        }
    }
}).mount("#app")