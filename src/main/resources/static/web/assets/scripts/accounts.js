const { createApp } = Vue

createApp({
    data() {
        return {
            clients: [],
            accounts: []
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            axios.get(`http://localhost:8080/api/clients/1`)
                .then(response => {
                    this.clients = response.data
                    this.clients_accounts = this.clients.accounts
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
        }
    }
}).mount("#app")