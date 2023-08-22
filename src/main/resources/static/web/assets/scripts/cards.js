const { createApp } = Vue

createApp({
    data() {
        return {
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
            axios.get(`http://localhost:8080/api/clients/current`)
                .then(response => {
                    this.clients = response.data
                    this.cards = this.clients.cards
                    this.debit= this.cards.filter(card=> card.type == "DEBIT")
                    this.credit= this.cards.filter(card=> card.type == "CREDIT")

                    this.fromDateDebit = this.debit.map(date => date.fromDate.slice(2,7))
                    this.thruDateDebit = this.debit.map(date => date.thruDate.slice(2,7))
                    
                    this.fromDateCredit = this.credit.map(date => date.fromDate.slice(2,7))
                    this.thruDateCredit= this.credit.map(date => date.thruDate.slice(2,7))
                
                    localStorage.setItem('client',JSON.stringify(this.clients)) 
                })
        },
        logOut(){
            axios.post("/api/logout")
            .then(response =>{
                location.href = "../../index.html"
            })
            .catch(error=> console.log(error.message))
        }
    }
}).mount("#app")