package com.github.kamicloud.stub.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@ConfigurationProperties(prefix = "stub-api", ignoreUnknownFields = false)
@Component
@SuppressWarnings("unused")
public class StubApiCoreProperties {
    private Process process = new Process();
    private String env;
    private String apiPrefix;
    private String templatePath = "./src/main/java/templates";
    private String testHost;
    private Generators generators = new Generators();

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getApiPrefix() {
        return apiPrefix;
    }

    public void setApiPrefix(String apiPrefix) {
        this.apiPrefix = apiPrefix;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public String getTestHost() {
        return testHost;
    }

    public void setTestHost(String testHost) {
        this.testHost = testHost;
    }

    public Generators getGenerators() {
        return generators;
    }

    public void setGenerators(Generators generators) {
        this.generators = generators;
    }

    public static class Process {
        private ArrayList<String> defaults;
        private ArrayList<String> laravelAutoTest;
        private ArrayList<String> client;
        private ArrayList<String> generators;

        public ArrayList<String> getDefaults() {
            return defaults;
        }

        public void setDefaults(ArrayList<String> defaults) {
            this.defaults = defaults;
        }

        public ArrayList<String> getLaravelAutoTest() {
            return laravelAutoTest;
        }

        public void setLaravelAutoTest(ArrayList<String> laravelAutoTest) {
            this.laravelAutoTest = laravelAutoTest;
        }

        public ArrayList<String> getClient() {
            return client;
        }

        public void setClient(ArrayList<String> client) {
            this.client = client;
        }

        public ArrayList<String> getGenerators() {
            return generators;
        }

        public void setGenerators(ArrayList<String> generators) {
            this.generators = generators;
        }
    }

    public static class Generators {
        private String forceSuffix;
        private Postman postman = new Postman();
        private Testcases testcases;
        private Laravel laravel;
        private LaravelDoc laravelDoc;
        private LaravelAutoTest laravelAutoTest;
        private NodejsClient nodejsClient;
        private JavaClient javaClient;
        private Openapi openapi;

        public String getForceSuffix() {
            return forceSuffix;
        }

        public void setForceSuffix(String forceSuffix) {
            this.forceSuffix = forceSuffix;
        }

        public Postman getPostman() {
            return postman;
        }

        public void setPostman(Postman postman) {
            this.postman = postman;
        }

        public Testcases getTestcases() {
            return testcases;
        }

        public void setTestcases(Testcases testcases) {
            this.testcases = testcases;
        }

        public Laravel getLaravel() {
            return laravel;
        }

        public void setLaravel(Laravel laravel) {
            this.laravel = laravel;
        }

        public LaravelDoc getLaravelDoc() {
            return laravelDoc;
        }

        public void setLaravelDoc(LaravelDoc laravelDoc) {
            this.laravelDoc = laravelDoc;
        }

        public JavaClient getJavaClient() {
            return javaClient;
        }

        public void setJavaClient(JavaClient javaClient) {
            this.javaClient = javaClient;
        }

        public NodejsClient getNodejsClient() {
            return nodejsClient;
        }

        public void setNodejsClient(NodejsClient nodejsClient) {
            this.nodejsClient = nodejsClient;
        }

        public LaravelAutoTest getLaravelAutoTest() {
            return laravelAutoTest;
        }

        public void setLaravelAutoTest(LaravelAutoTest laravelAutoTest) {
            this.laravelAutoTest = laravelAutoTest;
        }

        public Openapi getOpenapi() {
            return openapi;
        }

        public void setOpenapi(Openapi openapi) {
            this.openapi = openapi;
        }

        public static class Postman {
            private String path;

            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
            }
        }

        public static class Testcases {
            private String path;

            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
            }
        }

        public static class Openapi {
            private String path;

            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
            }
        }

        public static class Laravel {
            private String path;
            private String boSuffix;
            private String boFolder;
            private String dtoSuffix = "DTO";
            private String dtoFolder = "DTOs";
            private String serviceSuffix;
            private String serviceFolder;
            private String valueHelperNamespace;
            private String baseDtoNamespace;
            private String baseMessageNamespace;
            private String baseEnumNamespace;
            private String baseExceptionNamespace;

            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
            }

            public String getBoSuffix() {
                return boSuffix;
            }

            public void setBoSuffix(String boSuffix) {
                this.boSuffix = boSuffix;
            }

            public String getBoFolder() {
                return boFolder;
            }

            public void setBoFolder(String boFolder) {
                this.boFolder = boFolder;
            }

            public String getDtoSuffix() {
                return dtoSuffix;
            }

            public void setDtoSuffix(String dtoSuffix) {
                this.dtoSuffix = dtoSuffix;
            }

            public String getDtoFolder() {
                return dtoFolder;
            }

            public void setDtoFolder(String dtoFolder) {
                this.dtoFolder = dtoFolder;
            }

            public String getServiceSuffix() {
                return serviceSuffix;
            }

            public void setServiceSuffix(String serviceSuffix) {
                this.serviceSuffix = serviceSuffix;
            }

            public String getServiceFolder() {
                return serviceFolder;
            }

            public void setServiceFolder(String serviceFolder) {
                this.serviceFolder = serviceFolder;
            }

            public String getValueHelperNamespace() {
                return valueHelperNamespace;
            }

            public void setValueHelperNamespace(String valueHelperNamespace) {
                this.valueHelperNamespace = valueHelperNamespace;
            }

            public String getBaseDtoNamespace() {
                return baseDtoNamespace;
            }

            public void setBaseDtoNamespace(String baseDtoNamespace) {
                this.baseDtoNamespace = baseDtoNamespace;
            }

            public String getBaseMessageNamespace() {
                return baseMessageNamespace;
            }

            public void setBaseMessageNamespace(String baseMessageNamespace) {
                this.baseMessageNamespace = baseMessageNamespace;
            }

            public String getBaseEnumNamespace() {
                return baseEnumNamespace;
            }

            public void setBaseEnumNamespace(String baseEnumNamespace) {
                this.baseEnumNamespace = baseEnumNamespace;
            }

            public String getBaseExceptionNamespace() {
                return baseExceptionNamespace;
            }

            public void setBaseExceptionNamespace(String baseExceptionNamespace) {
                this.baseExceptionNamespace = baseExceptionNamespace;
            }
        }

        public static class LaravelDoc {
            private String path;
            private String httpPrefix;

            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
            }

            public String getHttpPrefix() {
                return httpPrefix;
            }

            public void setHttpPrefix(String httpPrefix) {
                this.httpPrefix = httpPrefix;
            }
        }

        public static class LaravelAutoTest {
            private String path;
            private boolean forceReplace;
            private String testcasesPath;

            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
            }

            public boolean isForceReplace() {
                return forceReplace;
            }

            public void setForceReplace(boolean forceReplace) {
                this.forceReplace = forceReplace;
            }

            public String getTestcasesPath() {
                return testcasesPath;
            }

            public void setTestcasesPath(String testcasesPath) {
                this.testcasesPath = testcasesPath;
            }
        }

        public static class NodejsClient {
            private String output;

            public String getOutput() {
                return output;
            }

            public void setOutput(String output) {
                this.output = output;
            }
        }

        public static class JavaClient {
            private String path;
            private String classpath;

            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
            }

            public String getClasspath() {
                return classpath;
            }

            public void setClasspath(String classpath) {
                this.classpath = classpath;
            }
        }
    }
}
