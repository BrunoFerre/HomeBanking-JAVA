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
    }
}
}).mount("#app")