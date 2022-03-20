import org.gradle.api.Project

class TaskTest implements org.gradle.api.Plugin<Project> {

    @Override
    void apply(Project project) {
        project.tasks.register("testingtask"){
            print "this is plugin task which is to be executed..."
        }

    }
}