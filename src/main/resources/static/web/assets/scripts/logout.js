export function logout(){
    axios.post("/api/logout")
    .then(response =>{
        location.href = "../../index.html"
    })
    .catch(error=> console.log(error.message))
}