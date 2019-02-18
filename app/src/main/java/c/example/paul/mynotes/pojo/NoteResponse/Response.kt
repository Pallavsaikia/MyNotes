package c.example.paul.mynotes.pojo.NoteResponse

data class Response(
    val statusCode: String,
    val statusDescription: String,
    val `data`: Data
)