import org.kohsuke.github.GitHub

class GitInterface(private val github: GitHub) {

    fun gatherRepo(repo: String): Repository =
            github.getRepository(repo).run {
        RepositoryImpl(name, listLanguages().keys.toList()).apply {

        }
    }

    fun gatherRepo(user: String, id: String) = gatherRepo("$user/$id")

}

abstract class Repository {

    abstract val name: String

    abstract val languages: List<String>

    override fun toString(): String {
        return "$name / $languages"
    }

}

private class RepositoryImpl(override val name: String, override val languages: List<String>) : Repository() {

}