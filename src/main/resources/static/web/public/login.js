const {createApp} = Vue

createApp({
    data(){
        return{
            firstName:'',
            lastName:'',
            email:'',
            password:'',
            client:[]
        }
    },
    methods:{
        logIn(){
            axios.post('/api/login','email='+ this.email + '&password='+this.password)
            .then((response)=>{
                if (this.email.includes("@admin.com")) {
                   location.href = "../adminPages/manager.html"
                }else{
                    location.href='../assets/pages/accounts.html'
                }
                this.client=response.data
            })  .catch(error =>  {
                console.log(error);
                Swal.fire({
                  title: 'Error!',
                  icon: 'error',
                  text: error.response.data,
                  confirmButtonText: 'Cool'
                })
              })
        }
    }
}).mount("#app")