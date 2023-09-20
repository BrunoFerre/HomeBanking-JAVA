const { createApp } = Vue
import { logout } from './logout.js'
createApp({
    data() {
        return {
            clients:[],
            client: [],
            cards:[],
            debit:[],
            credit:[],
            fromDateDebit:[],
            thruDateDebit:[],
            fromDateCredit:[],
            thruDateCredit:[],
            number:'',
            dateNew: new Date().toISOString().slice(2, 7)
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            this.client= JSON.parse(localStorage.getItem('client'))??[]
            axios.get(`http://localhost:8080/api/clients/current`)
                .then(response => {
                    this.clients = response.data
                    const cards = this.clients.cards
                    this.cards = cards.filter(card=> card.status == true)
                    this.debit= this.cards.filter(card=> card.type == "DEBIT")
                    this.credit= this.cards.filter(card=> card.type == "CREDIT")
                    this.fromDateDebit = this.debit.map(date => date.fromDate.slice(2,7))
                    this.thruDateDebit = this.debit.map(date => date.thruDate.slice(2,7))
                    
                    this.fromDateCredit = this.credit.map(date => date.fromDate.slice(2,7))
                    this.thruDateCredit= this.credit.map(date => date.thruDate.slice(2,7))
                    console.log(this.thruDateCredit);
                    console.log(this.dateNew);
                    localStorage.setItem('client',JSON.stringify(this.clients)) 
                })
        },
        logOut(){
            logout()
        },
        removeCard(number){ {
            Swal.fire({
                title: 'Delete this Card?',
                inputAttributes: {
                    autocapitalize: 'off',
                },
                showCancelButton: true,
                confirmButtonText: 'Yes',
                showLoaderOnConfirm: true,
                preConfirm: login => {
                    return axios.put(`http://localhost:8080/api/clients/current/cards`,`number=${number}`)
                        .then(response => {
                            setTimeout(() =>{
                            location.reload()
                            },200)
                        }).catch(error => {
                            console.log(error);
                            Swal.fire({
                                icon: 'error',
                                title: 'Oops...',
                                text: error.response.data,
                                confirmButtonColor: '#5b31be93',
                            })
                })
                }
            })
        }
    }
    }
}).mount("#app")