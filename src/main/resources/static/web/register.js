const { createApp } = Vue;
createApp({
  data() {
    return {
      firstName: "",
      lastName: "",
      email: "",
      password: "",
    };
  },
  created() {
    console.log("no te calentes martin");

  },
  methods: {
    prevent(event){
      event.preventDefault()
    },
    logIn(){
        axios.post("/api/login",`email=${this.email}&password=${this.password}`,{headers:{'content-type': 'application/x-www-form-urlencoded'}})
        .then((response)=>{
            if (this.email =="admin@admin.com") {
               location.href = "./adminPages/manager.html"
            }else{
                location.href='./assets/pages/accounts.html'
            }
        })
        .catch(error => alert(error.message))
    }, 
    register(event) {
      event.preventDefault();
      console.log("no te calentes martin");
      axios
      .post("/api/clients",`firstName=${this.firstName}&lastName=${this.lastName}&email=${this.email}&password=${this.password}`,{headers:{'content-type': 'application/x-www-form-urlencoded'}})
        .then((response) =>{
            this.logIn()
        }) 
        .catch((error) => alert(error.message));
    },
  },
}).mount("#app")
