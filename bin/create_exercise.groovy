final String exerciseName = args[0]

def path = new File(System.getProperty("user.dir"))
def exerciseDir = new File("${path}${File.separator}exercises")

def fileName = exerciseName.tokenize('-')*.toLowerCase()*.capitalize().join('')

def metaPath = [exerciseDir, exerciseName, ".meta", "src", "reference", "groovy"].join(File.separator)
def srcPath = [exerciseDir, exerciseName, "src", "main", "groovy"].join(File.separator)
def testPath = [exerciseDir, exerciseName, "src", "test", "groovy"].join(File.separator)

[metaPath, srcPath, testPath].each { new File(it).mkdirs() }

def build = '''apply plugin: "groovy"

repositories {
    mavenCentral()
}

dependencies {
    testCompile "org.spockframework:spock-core:1.3-groovy-2.5"
    compile "org.codehaus.groovy:groovy-all:2.5.8"
}

test {
    testLogging {
        exceptionFormat = 'full'
        events = ["passed", "failed", "skipped"]
    }
}'''

def exerciseClass = "class ${fileName} {\n\n}"

def spec = "import spock.lang.*\n\nclass ${fileName}Spec extends Specification {\n\n}"

def buildGradleFile = [exerciseDir, exerciseName, "build.gradle"].join(File.separator)
new File(buildGradleFile).withWriter { it << build }

[metaPath, srcPath].each {
    new File("${it}${File.separator}${fileName}.groovy").withWriter { it << exerciseClass }
}
new File("${testPath}${File.separator}${fileName}Spec.groovy").withWriter { it << spec }
