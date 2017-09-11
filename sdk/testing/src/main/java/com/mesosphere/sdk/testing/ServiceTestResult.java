package com.mesosphere.sdk.testing;

import java.util.Collection;
import java.util.Map;

import com.mesosphere.sdk.specification.ServiceSpec;
import com.mesosphere.sdk.specification.yaml.RawServiceSpec;

/**
 * An object which contains the generated results from rendering a Service via {@link ServiceTestBuilder}.
 */
public class ServiceTestResult {

    /**
     * An internal-only object for the result of generating a config file.
     */
    static class TaskConfig {
        private final String podType;
        private final String taskName;
        private final String configName;
        private final String configContent;

        TaskConfig(String podType, String taskName, String configName, String configContent) {
            this.podType = podType;
            this.taskName = taskName;
            this.configName = configName;
            this.configContent = configContent;
        }

        @Override
        public String toString() {
            return String.format("%s-%s: %s (%d bytes)", podType, taskName, configName, configContent.length());
        }
    }

    private final ServiceSpec serviceSpec;
    private final RawServiceSpec rawServiceSpec;
    private final Map<String, String> schedulerEnvironment;
    private final Collection<TaskConfig> taskConfigs;

    ServiceTestResult(
            ServiceSpec serviceSpec,
            RawServiceSpec rawServiceSpec,
            Map<String, String> schedulerEnvironment,
            Collection<TaskConfig> taskConfigs) {
        this.serviceSpec = serviceSpec;
        this.rawServiceSpec = rawServiceSpec;
        this.schedulerEnvironment = schedulerEnvironment;
        this.taskConfigs = taskConfigs;
    }

    /**
     * Returns the {@link ServiceSpec} (translated Service Specification) which was generated by the test.
     */
    public ServiceSpec getServiceSpec() {
        return serviceSpec;
    }

    /**
     * Returns the {@link RawServiceSpec} (object model of a {@code svc.yml}) which was generated by the test.
     */
    public RawServiceSpec getRawServiceSpec() {
        return rawServiceSpec;
    }

    /**
     * Returns the Scheduler environment which was generated by the test.
     */
    public Map<String, String> getSchedulerEnvironment() {
        return schedulerEnvironment;
    }

    /**
     * Returns the specified rendered task config content, or throws {@link IllegalArgumentException} if no such config
     * was found.
     */
    public String getTaskConfig(String podType, String taskName, String configName) {
        for (TaskConfig config : taskConfigs) {
            if (config.podType.equals(podType)
                    && config.taskName.equals(taskName)
                    && config.configName.equals(configName)) {
                return config.configContent;
            }
        }
        throw new IllegalArgumentException(String.format(
                "Unable to find config [pod=%s, task=%s, config=%s]. Known configs are: %s",
                podType, taskName, configName, taskConfigs));
    }
}
