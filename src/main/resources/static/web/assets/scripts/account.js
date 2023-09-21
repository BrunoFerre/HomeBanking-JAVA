const { createApp } = Vue
import { logout } from './logout.js'
createApp({
    data() {
        return {
            client: [],
            account: [],
            id_account: Number,
            transaction:[],
            error:'',
            nuevoArray:[],
            dateAsherter:[],
            dateInit:'',
            dateEnd:'',
            numberAcc:'',
            showForm: false
        }
    },
    created() {
        this.loadData() 
    },
    methods: {
        loadData() {
            this.client= JSON.parse(localStorage.getItem('client'))??[]
            console.log(this.client);
            const parameter = location.search
            const parameterUrl = new URLSearchParams(parameter)
            this.id_account = parameterUrl.get("id")
            axios.get(`http://localhost:8080/api/clients/accounts/${this.id_account}`)
                .then(response => {
                    this.account = response.data
                    if(this.account.status == false){
                        this.account=[]
                    }      
                    for(let transaction of this.account.transactions){
                        this.transaction.push(transaction)
                    }
                    this.dateAsherter=this.transaction.map(transaction=>transaction.date.slice(2,-3).replace(/-/g, '/'))
                    
                    this.transaction.sort((a,b)=> a.id - b.id)
                    console.log(this.transaction);
            }).catch(error => {
                this.error= error.message
                console.log(this.error);
                // location.href = "../pages/error.html"
            })
        },
        logOut(){
            logout()
        },
        deleteAccount(){ {
            Swal.fire({
                title: 'Delete this account?',
                inputAttributes: {
                    autocapitalize: 'off',
                },
                showCancelButton: true,
                confirmButtonText: 'Yes',
                showLoaderOnConfirm: true,
                preConfirm: login => {
                    return axios
                        .put(`/api/clients/current/accounts/${this.id_account}`)
                        .then(response => {
                            setTimeout(() =>{
                            location.href = "../pages/accounts.html";  
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
    },
    searchTransactions() {
        const url = `http://localhost:8080/api/transactions/findDate?dateInit=${this.dateInit}:00&dateEnd=${this.dateEnd}:00&numberAcc=${this.account.number}`;
        axios
            .get(url, { responseType: 'blob' })
            .then((response) => {
                console.log(response);
                const blob = new Blob([response.data], { type: 'application/pdf' });
                const url = window.URL.createObjectURL(blob);
                const a = document.createElement('a');
                a.href = url;
                a.download = 'transactions-Table.pdf';
                a.click();
                window.URL.revokeObjectURL(url);
            })
            .catch(error =>
                console.error(error)
            );
    },
    showOption(){
        this.showForm=!this.showForm
    }

}
}).mount("#app")