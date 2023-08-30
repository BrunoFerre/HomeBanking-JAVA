const { createApp } = Vue
import { logout } from './logout.js'
createApp({
    data() {
        return {
            client:[],
            clients: [],
            cards:[],
            debit:[],
            credit:[],
            fromDateDebit:[],
            thruDateDebit:[],
            fromDateCredit:[],
            thruDateCredit:[]
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
                    console.log(this.clients);
                    this.cards = this.clients.cards
                    this.debit= this.cards.filter(card=> card.type == "DEBIT")
                    this.credit= this.cards.filter(card=> card.type == "CREDIT")
                    console.log(this.debit);
                    this.fromDateDebit = this.debit.map(date => date.fromDate.slice(2,7))
                    this.thruDateDebit = this.debit.map(date => date.thruDate.slice(2,7))
                    this.fromDateCredit = this.credit.map(date => date.fromDate.slice(2,7))
                    this.thruDateCredit= this.credit.map(date => date.thruDate.slice(2,7))
                    localStorage.setItem('client',JSON.stringify(this.clients)) 
                })
        },
        logOut(){
            logout()
        },
    }
}).mount("#app")