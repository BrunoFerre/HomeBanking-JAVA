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

  },
  methods: {
    prevent(event) {
      event.preventDefault()
    },
    logIn() {
      axios.post("/api/login", `email=${this.email}&password=${this.password}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
        .then((response) => {
          if (this.email == "admin@admin.com") {
            location.href = "./adminPages/manager.html"
          } else {
            location.href = './assets/pages/accounts.html'
          }
        })
        .catch(error => (error.message))
    },
    register(event){
      event.preventDefault()
      Swal.fire({
        title: 'Register Mind Hub Brother?',
        inputAttributes: {
          autocapitalize: 'off',
        },
        showCancelButton: true,
        confirmButtonText: 'Yes',
        showLoaderOnConfirm: true,
        preConfirm: login => {
          return axios
            .post("/api/clients", `firstName=${this.firstName}&lastName=${this.lastName}&email=${this.email}&password=${this.password}`,
              { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
            .then(response => {
              setTimeout(() => {
                this.logIn();
              }, 2000)
            })
            .catch(error => {
              Swal.fire({
                icon: 'error',
                text: error.response.data,
                confirmButtonColor: '#5b31be93',
              })
            });
        },
        allowOutsideClick: () => !Swal.isLoading(),
      });
      }
  },
}).mount("#app")

