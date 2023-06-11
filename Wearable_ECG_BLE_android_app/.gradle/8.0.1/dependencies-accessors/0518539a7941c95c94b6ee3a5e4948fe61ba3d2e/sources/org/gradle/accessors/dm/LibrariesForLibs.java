package org.gradle.accessors.dm;

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.MinimalExternalModuleDependency;
import org.gradle.plugin.use.PluginDependency;
import org.gradle.api.artifacts.ExternalModuleDependencyBundle;
import org.gradle.api.artifacts.MutableVersionConstraint;
import org.gradle.api.provider.Provider;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.internal.catalog.AbstractExternalDependencyFactory;
import org.gradle.api.internal.catalog.DefaultVersionCatalog;
import java.util.Map;
import org.gradle.api.internal.attributes.ImmutableAttributesFactory;
import org.gradle.api.internal.artifacts.dsl.CapabilityNotationParser;
import javax.inject.Inject;

/**
 * A catalog of dependencies accessible via the `libs` extension.
*/
@NonNullApi
public class LibrariesForLibs extends AbstractExternalDependencyFactory {

    private final AbstractExternalDependencyFactory owner = this;
    private final AccompanistLibraryAccessors laccForAccompanistLibraryAccessors = new AccompanistLibraryAccessors(owner);
    private final AndroidLibraryAccessors laccForAndroidLibraryAccessors = new AndroidLibraryAccessors(owner);
    private final AndroidxLibraryAccessors laccForAndroidxLibraryAccessors = new AndroidxLibraryAccessors(owner);
    private final CoilLibraryAccessors laccForCoilLibraryAccessors = new CoilLibraryAccessors(owner);
    private final FirebaseLibraryAccessors laccForFirebaseLibraryAccessors = new FirebaseLibraryAccessors(owner);
    private final HiltLibraryAccessors laccForHiltLibraryAccessors = new HiltLibraryAccessors(owner);
    private final KotlinLibraryAccessors laccForKotlinLibraryAccessors = new KotlinLibraryAccessors(owner);
    private final KotlinxLibraryAccessors laccForKotlinxLibraryAccessors = new KotlinxLibraryAccessors(owner);
    private final LintLibraryAccessors laccForLintLibraryAccessors = new LintLibraryAccessors(owner);
    private final MemfaultLibraryAccessors laccForMemfaultLibraryAccessors = new MemfaultLibraryAccessors(owner);
    private final NordicLibraryAccessors laccForNordicLibraryAccessors = new NordicLibraryAccessors(owner);
    private final OkhttpLibraryAccessors laccForOkhttpLibraryAccessors = new OkhttpLibraryAccessors(owner);
    private final ProtobufLibraryAccessors laccForProtobufLibraryAccessors = new ProtobufLibraryAccessors(owner);
    private final RetrofitLibraryAccessors laccForRetrofitLibraryAccessors = new RetrofitLibraryAccessors(owner);
    private final RoomLibraryAccessors laccForRoomLibraryAccessors = new RoomLibraryAccessors(owner);
    private final VersionAccessors vaccForVersionAccessors = new VersionAccessors(providers, config);
    private final BundleAccessors baccForBundleAccessors = new BundleAccessors(objects, providers, config, attributesFactory, capabilityNotationParser);
    private final PluginAccessors paccForPluginAccessors = new PluginAccessors(providers, config);

    @Inject
    public LibrariesForLibs(DefaultVersionCatalog config, ProviderFactory providers, ObjectFactory objects, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) {
        super(config, providers, objects, attributesFactory, capabilityNotationParser);
    }

        /**
         * Creates a dependency provider for chart (com.github.PhilJay:MPAndroidChart)
         * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
         */
        public Provider<MinimalExternalModuleDependency> getChart() { return create("chart"); }

        /**
         * Creates a dependency provider for junit4 (junit:junit)
         * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
         */
        public Provider<MinimalExternalModuleDependency> getJunit4() { return create("junit4"); }

        /**
         * Creates a dependency provider for leakcanary (com.squareup.leakcanary:leakcanary-android)
         * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
         */
        public Provider<MinimalExternalModuleDependency> getLeakcanary() { return create("leakcanary"); }

        /**
         * Creates a dependency provider for markdown (com.github.jeziellago:compose-markdown)
         * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
         */
        public Provider<MinimalExternalModuleDependency> getMarkdown() { return create("markdown"); }

        /**
         * Creates a dependency provider for timber (com.jakewharton.timber:timber)
         * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
         */
        public Provider<MinimalExternalModuleDependency> getTimber() { return create("timber"); }

        /**
         * Creates a dependency provider for turbine (app.cash.turbine:turbine)
         * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
         */
        public Provider<MinimalExternalModuleDependency> getTurbine() { return create("turbine"); }

    /**
     * Returns the group of libraries at accompanist
     */
    public AccompanistLibraryAccessors getAccompanist() { return laccForAccompanistLibraryAccessors; }

    /**
     * Returns the group of libraries at android
     */
    public AndroidLibraryAccessors getAndroid() { return laccForAndroidLibraryAccessors; }

    /**
     * Returns the group of libraries at androidx
     */
    public AndroidxLibraryAccessors getAndroidx() { return laccForAndroidxLibraryAccessors; }

    /**
     * Returns the group of libraries at coil
     */
    public CoilLibraryAccessors getCoil() { return laccForCoilLibraryAccessors; }

    /**
     * Returns the group of libraries at firebase
     */
    public FirebaseLibraryAccessors getFirebase() { return laccForFirebaseLibraryAccessors; }

    /**
     * Returns the group of libraries at hilt
     */
    public HiltLibraryAccessors getHilt() { return laccForHiltLibraryAccessors; }

    /**
     * Returns the group of libraries at kotlin
     */
    public KotlinLibraryAccessors getKotlin() { return laccForKotlinLibraryAccessors; }

    /**
     * Returns the group of libraries at kotlinx
     */
    public KotlinxLibraryAccessors getKotlinx() { return laccForKotlinxLibraryAccessors; }

    /**
     * Returns the group of libraries at lint
     */
    public LintLibraryAccessors getLint() { return laccForLintLibraryAccessors; }

    /**
     * Returns the group of libraries at memfault
     */
    public MemfaultLibraryAccessors getMemfault() { return laccForMemfaultLibraryAccessors; }

    /**
     * Returns the group of libraries at nordic
     */
    public NordicLibraryAccessors getNordic() { return laccForNordicLibraryAccessors; }

    /**
     * Returns the group of libraries at okhttp
     */
    public OkhttpLibraryAccessors getOkhttp() { return laccForOkhttpLibraryAccessors; }

    /**
     * Returns the group of libraries at protobuf
     */
    public ProtobufLibraryAccessors getProtobuf() { return laccForProtobufLibraryAccessors; }

    /**
     * Returns the group of libraries at retrofit
     */
    public RetrofitLibraryAccessors getRetrofit() { return laccForRetrofitLibraryAccessors; }

    /**
     * Returns the group of libraries at room
     */
    public RoomLibraryAccessors getRoom() { return laccForRoomLibraryAccessors; }

    /**
     * Returns the group of versions at versions
     */
    public VersionAccessors getVersions() { return vaccForVersionAccessors; }

    /**
     * Returns the group of bundles at bundles
     */
    public BundleAccessors getBundles() { return baccForBundleAccessors; }

    /**
     * Returns the group of plugins at plugins
     */
    public PluginAccessors getPlugins() { return paccForPluginAccessors; }

    public static class AccompanistLibraryAccessors extends SubDependencyFactory {

        public AccompanistLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for flowlayout (com.google.accompanist:accompanist-flowlayout)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getFlowlayout() { return create("accompanist.flowlayout"); }

            /**
             * Creates a dependency provider for pager (com.google.accompanist:accompanist-pager)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getPager() { return create("accompanist.pager"); }

            /**
             * Creates a dependency provider for pagerindicators (com.google.accompanist:accompanist-pager-indicators)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getPagerindicators() { return create("accompanist.pagerindicators"); }

            /**
             * Creates a dependency provider for placeholder (com.google.accompanist:accompanist-placeholder-material)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getPlaceholder() { return create("accompanist.placeholder"); }

            /**
             * Creates a dependency provider for swiperefresh (com.google.accompanist:accompanist-swiperefresh)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getSwiperefresh() { return create("accompanist.swiperefresh"); }

            /**
             * Creates a dependency provider for systemuicontroller (com.google.accompanist:accompanist-systemuicontroller)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getSystemuicontroller() { return create("accompanist.systemuicontroller"); }

    }

    public static class AndroidLibraryAccessors extends SubDependencyFactory {

        public AndroidLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for desugarJdkLibs (com.android.tools:desugar_jdk_libs)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getDesugarJdkLibs() { return create("android.desugarJdkLibs"); }

            /**
             * Creates a dependency provider for gradlePlugin (com.android.tools.build:gradle)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getGradlePlugin() { return create("android.gradlePlugin"); }

    }

    public static class AndroidxLibraryAccessors extends SubDependencyFactory {
        private final AndroidxActivityLibraryAccessors laccForAndroidxActivityLibraryAccessors = new AndroidxActivityLibraryAccessors(owner);
        private final AndroidxBenchmarkLibraryAccessors laccForAndroidxBenchmarkLibraryAccessors = new AndroidxBenchmarkLibraryAccessors(owner);
        private final AndroidxComposeLibraryAccessors laccForAndroidxComposeLibraryAccessors = new AndroidxComposeLibraryAccessors(owner);
        private final AndroidxCoreLibraryAccessors laccForAndroidxCoreLibraryAccessors = new AndroidxCoreLibraryAccessors(owner);
        private final AndroidxDataStoreLibraryAccessors laccForAndroidxDataStoreLibraryAccessors = new AndroidxDataStoreLibraryAccessors(owner);
        private final AndroidxHiltLibraryAccessors laccForAndroidxHiltLibraryAccessors = new AndroidxHiltLibraryAccessors(owner);
        private final AndroidxLifecycleLibraryAccessors laccForAndroidxLifecycleLibraryAccessors = new AndroidxLifecycleLibraryAccessors(owner);
        private final AndroidxNavigationLibraryAccessors laccForAndroidxNavigationLibraryAccessors = new AndroidxNavigationLibraryAccessors(owner);
        private final AndroidxTestLibraryAccessors laccForAndroidxTestLibraryAccessors = new AndroidxTestLibraryAccessors(owner);
        private final AndroidxTracingLibraryAccessors laccForAndroidxTracingLibraryAccessors = new AndroidxTracingLibraryAccessors(owner);
        private final AndroidxWindowLibraryAccessors laccForAndroidxWindowLibraryAccessors = new AndroidxWindowLibraryAccessors(owner);
        private final AndroidxWorkLibraryAccessors laccForAndroidxWorkLibraryAccessors = new AndroidxWorkLibraryAccessors(owner);

        public AndroidxLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for annotation (androidx.annotation:annotation)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getAnnotation() { return create("androidx.annotation"); }

            /**
             * Creates a dependency provider for appcompat (androidx.appcompat:appcompat)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getAppcompat() { return create("androidx.appcompat"); }

            /**
             * Creates a dependency provider for localbroadcastmanager (androidx.localbroadcastmanager:localbroadcastmanager)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getLocalbroadcastmanager() { return create("androidx.localbroadcastmanager"); }

            /**
             * Creates a dependency provider for metrics (androidx.metrics:metrics-performance)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getMetrics() { return create("androidx.metrics"); }

            /**
             * Creates a dependency provider for profileinstaller (androidx.profileinstaller:profileinstaller)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getProfileinstaller() { return create("androidx.profileinstaller"); }

            /**
             * Creates a dependency provider for startup (androidx.startup:startup-runtime)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getStartup() { return create("androidx.startup"); }

        /**
         * Returns the group of libraries at androidx.activity
         */
        public AndroidxActivityLibraryAccessors getActivity() { return laccForAndroidxActivityLibraryAccessors; }

        /**
         * Returns the group of libraries at androidx.benchmark
         */
        public AndroidxBenchmarkLibraryAccessors getBenchmark() { return laccForAndroidxBenchmarkLibraryAccessors; }

        /**
         * Returns the group of libraries at androidx.compose
         */
        public AndroidxComposeLibraryAccessors getCompose() { return laccForAndroidxComposeLibraryAccessors; }

        /**
         * Returns the group of libraries at androidx.core
         */
        public AndroidxCoreLibraryAccessors getCore() { return laccForAndroidxCoreLibraryAccessors; }

        /**
         * Returns the group of libraries at androidx.dataStore
         */
        public AndroidxDataStoreLibraryAccessors getDataStore() { return laccForAndroidxDataStoreLibraryAccessors; }

        /**
         * Returns the group of libraries at androidx.hilt
         */
        public AndroidxHiltLibraryAccessors getHilt() { return laccForAndroidxHiltLibraryAccessors; }

        /**
         * Returns the group of libraries at androidx.lifecycle
         */
        public AndroidxLifecycleLibraryAccessors getLifecycle() { return laccForAndroidxLifecycleLibraryAccessors; }

        /**
         * Returns the group of libraries at androidx.navigation
         */
        public AndroidxNavigationLibraryAccessors getNavigation() { return laccForAndroidxNavigationLibraryAccessors; }

        /**
         * Returns the group of libraries at androidx.test
         */
        public AndroidxTestLibraryAccessors getTest() { return laccForAndroidxTestLibraryAccessors; }

        /**
         * Returns the group of libraries at androidx.tracing
         */
        public AndroidxTracingLibraryAccessors getTracing() { return laccForAndroidxTracingLibraryAccessors; }

        /**
         * Returns the group of libraries at androidx.window
         */
        public AndroidxWindowLibraryAccessors getWindow() { return laccForAndroidxWindowLibraryAccessors; }

        /**
         * Returns the group of libraries at androidx.work
         */
        public AndroidxWorkLibraryAccessors getWork() { return laccForAndroidxWorkLibraryAccessors; }

    }

    public static class AndroidxActivityLibraryAccessors extends SubDependencyFactory {

        public AndroidxActivityLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for compose (androidx.activity:activity-compose)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getCompose() { return create("androidx.activity.compose"); }

    }

    public static class AndroidxBenchmarkLibraryAccessors extends SubDependencyFactory {

        public AndroidxBenchmarkLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for macro (androidx.benchmark:benchmark-macro-junit4)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getMacro() { return create("androidx.benchmark.macro"); }

    }

    public static class AndroidxComposeLibraryAccessors extends SubDependencyFactory {
        private final AndroidxComposeFoundationLibraryAccessors laccForAndroidxComposeFoundationLibraryAccessors = new AndroidxComposeFoundationLibraryAccessors(owner);
        private final AndroidxComposeMaterialLibraryAccessors laccForAndroidxComposeMaterialLibraryAccessors = new AndroidxComposeMaterialLibraryAccessors(owner);
        private final AndroidxComposeMaterial3LibraryAccessors laccForAndroidxComposeMaterial3LibraryAccessors = new AndroidxComposeMaterial3LibraryAccessors(owner);
        private final AndroidxComposeRuntimeLibraryAccessors laccForAndroidxComposeRuntimeLibraryAccessors = new AndroidxComposeRuntimeLibraryAccessors(owner);
        private final AndroidxComposeUiLibraryAccessors laccForAndroidxComposeUiLibraryAccessors = new AndroidxComposeUiLibraryAccessors(owner);

        public AndroidxComposeLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for bom (androidx.compose:compose-bom)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getBom() { return create("androidx.compose.bom"); }

        /**
         * Returns the group of libraries at androidx.compose.foundation
         */
        public AndroidxComposeFoundationLibraryAccessors getFoundation() { return laccForAndroidxComposeFoundationLibraryAccessors; }

        /**
         * Returns the group of libraries at androidx.compose.material
         */
        public AndroidxComposeMaterialLibraryAccessors getMaterial() { return laccForAndroidxComposeMaterialLibraryAccessors; }

        /**
         * Returns the group of libraries at androidx.compose.material3
         */
        public AndroidxComposeMaterial3LibraryAccessors getMaterial3() { return laccForAndroidxComposeMaterial3LibraryAccessors; }

        /**
         * Returns the group of libraries at androidx.compose.runtime
         */
        public AndroidxComposeRuntimeLibraryAccessors getRuntime() { return laccForAndroidxComposeRuntimeLibraryAccessors; }

        /**
         * Returns the group of libraries at androidx.compose.ui
         */
        public AndroidxComposeUiLibraryAccessors getUi() { return laccForAndroidxComposeUiLibraryAccessors; }

    }

    public static class AndroidxComposeFoundationLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public AndroidxComposeFoundationLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for foundation (androidx.compose.foundation:foundation)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> asProvider() { return create("androidx.compose.foundation"); }

            /**
             * Creates a dependency provider for layout (androidx.compose.foundation:foundation-layout)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getLayout() { return create("androidx.compose.foundation.layout"); }

    }

    public static class AndroidxComposeMaterialLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public AndroidxComposeMaterialLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for material (androidx.compose.material:material)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> asProvider() { return create("androidx.compose.material"); }

            /**
             * Creates a dependency provider for iconsExtended (androidx.compose.material:material-icons-extended)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getIconsExtended() { return create("androidx.compose.material.iconsExtended"); }

    }

    public static class AndroidxComposeMaterial3LibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public AndroidxComposeMaterial3LibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for material3 (androidx.compose.material3:material3)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> asProvider() { return create("androidx.compose.material3"); }

            /**
             * Creates a dependency provider for windowSizeClass (androidx.compose.material3:material3-window-size-class)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getWindowSizeClass() { return create("androidx.compose.material3.windowSizeClass"); }

    }

    public static class AndroidxComposeRuntimeLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public AndroidxComposeRuntimeLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for runtime (androidx.compose.runtime:runtime)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> asProvider() { return create("androidx.compose.runtime"); }

            /**
             * Creates a dependency provider for livedata (androidx.compose.runtime:runtime-livedata)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getLivedata() { return create("androidx.compose.runtime.livedata"); }

            /**
             * Creates a dependency provider for tracing (androidx.compose.runtime:runtime-tracing)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getTracing() { return create("androidx.compose.runtime.tracing"); }

    }

    public static class AndroidxComposeUiLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {
        private final AndroidxComposeUiToolingLibraryAccessors laccForAndroidxComposeUiToolingLibraryAccessors = new AndroidxComposeUiToolingLibraryAccessors(owner);

        public AndroidxComposeUiLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for ui (androidx.compose.ui:ui)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> asProvider() { return create("androidx.compose.ui"); }

            /**
             * Creates a dependency provider for test (androidx.compose.ui:ui-test-junit4)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getTest() { return create("androidx.compose.ui.test"); }

            /**
             * Creates a dependency provider for testManifest (androidx.compose.ui:ui-test-manifest)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getTestManifest() { return create("androidx.compose.ui.testManifest"); }

            /**
             * Creates a dependency provider for util (androidx.compose.ui:ui-util)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getUtil() { return create("androidx.compose.ui.util"); }

        /**
         * Returns the group of libraries at androidx.compose.ui.tooling
         */
        public AndroidxComposeUiToolingLibraryAccessors getTooling() { return laccForAndroidxComposeUiToolingLibraryAccessors; }

    }

    public static class AndroidxComposeUiToolingLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public AndroidxComposeUiToolingLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for tooling (androidx.compose.ui:ui-tooling)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> asProvider() { return create("androidx.compose.ui.tooling"); }

            /**
             * Creates a dependency provider for preview (androidx.compose.ui:ui-tooling-preview)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getPreview() { return create("androidx.compose.ui.tooling.preview"); }

    }

    public static class AndroidxCoreLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public AndroidxCoreLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for core (androidx.core:core)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> asProvider() { return create("androidx.core"); }

            /**
             * Creates a dependency provider for ktx (androidx.core:core-ktx)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getKtx() { return create("androidx.core.ktx"); }

            /**
             * Creates a dependency provider for splashscreen (androidx.core:core-splashscreen)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getSplashscreen() { return create("androidx.core.splashscreen"); }

    }

    public static class AndroidxDataStoreLibraryAccessors extends SubDependencyFactory {

        public AndroidxDataStoreLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for core (androidx.datastore:datastore)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getCore() { return create("androidx.dataStore.core"); }

            /**
             * Creates a dependency provider for preferences (androidx.datastore:datastore-preferences)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getPreferences() { return create("androidx.dataStore.preferences"); }

    }

    public static class AndroidxHiltLibraryAccessors extends SubDependencyFactory {
        private final AndroidxHiltNavigationLibraryAccessors laccForAndroidxHiltNavigationLibraryAccessors = new AndroidxHiltNavigationLibraryAccessors(owner);

        public AndroidxHiltLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at androidx.hilt.navigation
         */
        public AndroidxHiltNavigationLibraryAccessors getNavigation() { return laccForAndroidxHiltNavigationLibraryAccessors; }

    }

    public static class AndroidxHiltNavigationLibraryAccessors extends SubDependencyFactory {

        public AndroidxHiltNavigationLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for compose (androidx.hilt:hilt-navigation-compose)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getCompose() { return create("androidx.hilt.navigation.compose"); }

    }

    public static class AndroidxLifecycleLibraryAccessors extends SubDependencyFactory {
        private final AndroidxLifecycleLivedataLibraryAccessors laccForAndroidxLifecycleLivedataLibraryAccessors = new AndroidxLifecycleLivedataLibraryAccessors(owner);
        private final AndroidxLifecycleRuntimeLibraryAccessors laccForAndroidxLifecycleRuntimeLibraryAccessors = new AndroidxLifecycleRuntimeLibraryAccessors(owner);
        private final AndroidxLifecycleViewModelLibraryAccessors laccForAndroidxLifecycleViewModelLibraryAccessors = new AndroidxLifecycleViewModelLibraryAccessors(owner);

        public AndroidxLifecycleLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for service (androidx.lifecycle:lifecycle-service)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getService() { return create("androidx.lifecycle.service"); }

        /**
         * Returns the group of libraries at androidx.lifecycle.livedata
         */
        public AndroidxLifecycleLivedataLibraryAccessors getLivedata() { return laccForAndroidxLifecycleLivedataLibraryAccessors; }

        /**
         * Returns the group of libraries at androidx.lifecycle.runtime
         */
        public AndroidxLifecycleRuntimeLibraryAccessors getRuntime() { return laccForAndroidxLifecycleRuntimeLibraryAccessors; }

        /**
         * Returns the group of libraries at androidx.lifecycle.viewModel
         */
        public AndroidxLifecycleViewModelLibraryAccessors getViewModel() { return laccForAndroidxLifecycleViewModelLibraryAccessors; }

    }

    public static class AndroidxLifecycleLivedataLibraryAccessors extends SubDependencyFactory {

        public AndroidxLifecycleLivedataLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for ktx (androidx.lifecycle:lifecycle-livedata-ktx)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getKtx() { return create("androidx.lifecycle.livedata.ktx"); }

    }

    public static class AndroidxLifecycleRuntimeLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public AndroidxLifecycleRuntimeLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for runtime (androidx.lifecycle:lifecycle-runtime)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> asProvider() { return create("androidx.lifecycle.runtime"); }

            /**
             * Creates a dependency provider for compose (androidx.lifecycle:lifecycle-runtime-compose)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getCompose() { return create("androidx.lifecycle.runtime.compose"); }

            /**
             * Creates a dependency provider for ktx (androidx.lifecycle:lifecycle-runtime-ktx)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getKtx() { return create("androidx.lifecycle.runtime.ktx"); }

    }

    public static class AndroidxLifecycleViewModelLibraryAccessors extends SubDependencyFactory {

        public AndroidxLifecycleViewModelLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for compose (androidx.lifecycle:lifecycle-viewmodel-compose)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getCompose() { return create("androidx.lifecycle.viewModel.compose"); }

            /**
             * Creates a dependency provider for ktx (androidx.lifecycle:lifecycle-viewmodel-ktx)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getKtx() { return create("androidx.lifecycle.viewModel.ktx"); }

            /**
             * Creates a dependency provider for savedState (androidx.lifecycle:lifecycle-viewmodel-savedstate)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getSavedState() { return create("androidx.lifecycle.viewModel.savedState"); }

    }

    public static class AndroidxNavigationLibraryAccessors extends SubDependencyFactory {

        public AndroidxNavigationLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for compose (androidx.navigation:navigation-compose)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getCompose() { return create("androidx.navigation.compose"); }

            /**
             * Creates a dependency provider for testing (androidx.navigation:navigation-testing)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getTesting() { return create("androidx.navigation.testing"); }

    }

    public static class AndroidxTestLibraryAccessors extends SubDependencyFactory {
        private final AndroidxTestEspressoLibraryAccessors laccForAndroidxTestEspressoLibraryAccessors = new AndroidxTestEspressoLibraryAccessors(owner);

        public AndroidxTestLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for core (androidx.test:core)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getCore() { return create("androidx.test.core"); }

            /**
             * Creates a dependency provider for ext (androidx.test.ext:junit-ktx)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getExt() { return create("androidx.test.ext"); }

            /**
             * Creates a dependency provider for rules (androidx.test:rules)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getRules() { return create("androidx.test.rules"); }

            /**
             * Creates a dependency provider for runner (androidx.test:runner)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getRunner() { return create("androidx.test.runner"); }

            /**
             * Creates a dependency provider for uiautomator (androidx.test.uiautomator:uiautomator)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getUiautomator() { return create("androidx.test.uiautomator"); }

        /**
         * Returns the group of libraries at androidx.test.espresso
         */
        public AndroidxTestEspressoLibraryAccessors getEspresso() { return laccForAndroidxTestEspressoLibraryAccessors; }

    }

    public static class AndroidxTestEspressoLibraryAccessors extends SubDependencyFactory {

        public AndroidxTestEspressoLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for core (androidx.test.espresso:espresso-core)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getCore() { return create("androidx.test.espresso.core"); }

    }

    public static class AndroidxTracingLibraryAccessors extends SubDependencyFactory {

        public AndroidxTracingLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for ktx (androidx.tracing:tracing-ktx)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getKtx() { return create("androidx.tracing.ktx"); }

    }

    public static class AndroidxWindowLibraryAccessors extends SubDependencyFactory {

        public AndroidxWindowLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for manager (androidx.window:window)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getManager() { return create("androidx.window.manager"); }

    }

    public static class AndroidxWorkLibraryAccessors extends SubDependencyFactory {

        public AndroidxWorkLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for ktx (androidx.work:work-runtime-ktx)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getKtx() { return create("androidx.work.ktx"); }

            /**
             * Creates a dependency provider for testing (androidx.work:work-testing)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getTesting() { return create("androidx.work.testing"); }

    }

    public static class CoilLibraryAccessors extends SubDependencyFactory {
        private final CoilKtLibraryAccessors laccForCoilKtLibraryAccessors = new CoilKtLibraryAccessors(owner);

        public CoilLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at coil.kt
         */
        public CoilKtLibraryAccessors getKt() { return laccForCoilKtLibraryAccessors; }

    }

    public static class CoilKtLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public CoilKtLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for kt (io.coil-kt:coil)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> asProvider() { return create("coil.kt"); }

            /**
             * Creates a dependency provider for compose (io.coil-kt:coil-compose)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getCompose() { return create("coil.kt.compose"); }

            /**
             * Creates a dependency provider for svg (io.coil-kt:coil-svg)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getSvg() { return create("coil.kt.svg"); }

    }

    public static class FirebaseLibraryAccessors extends SubDependencyFactory {

        public FirebaseLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for analytics (com.google.firebase:firebase-analytics-ktx)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getAnalytics() { return create("firebase.analytics"); }

            /**
             * Creates a dependency provider for bom (com.google.firebase:firebase-bom)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getBom() { return create("firebase.bom"); }

            /**
             * Creates a dependency provider for crashlytics (com.google.firebase:firebase-crashlytics-ktx)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getCrashlytics() { return create("firebase.crashlytics"); }

    }

    public static class HiltLibraryAccessors extends SubDependencyFactory {
        private final HiltAndroidLibraryAccessors laccForHiltAndroidLibraryAccessors = new HiltAndroidLibraryAccessors(owner);
        private final HiltExtLibraryAccessors laccForHiltExtLibraryAccessors = new HiltExtLibraryAccessors(owner);

        public HiltLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for compiler (com.google.dagger:hilt-android-compiler)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getCompiler() { return create("hilt.compiler"); }

        /**
         * Returns the group of libraries at hilt.android
         */
        public HiltAndroidLibraryAccessors getAndroid() { return laccForHiltAndroidLibraryAccessors; }

        /**
         * Returns the group of libraries at hilt.ext
         */
        public HiltExtLibraryAccessors getExt() { return laccForHiltExtLibraryAccessors; }

    }

    public static class HiltAndroidLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public HiltAndroidLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for android (com.google.dagger:hilt-android)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> asProvider() { return create("hilt.android"); }

            /**
             * Creates a dependency provider for testing (com.google.dagger:hilt-android-testing)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getTesting() { return create("hilt.android.testing"); }

    }

    public static class HiltExtLibraryAccessors extends SubDependencyFactory {

        public HiltExtLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for compiler (androidx.hilt:hilt-compiler)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getCompiler() { return create("hilt.ext.compiler"); }

            /**
             * Creates a dependency provider for work (androidx.hilt:hilt-work)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getWork() { return create("hilt.ext.work"); }

    }

    public static class KotlinLibraryAccessors extends SubDependencyFactory {

        public KotlinLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for gradlePlugin (org.jetbrains.kotlin:kotlin-gradle-plugin)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getGradlePlugin() { return create("kotlin.gradlePlugin"); }

            /**
             * Creates a dependency provider for stdlib (org.jetbrains.kotlin:kotlin-stdlib-jdk8)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getStdlib() { return create("kotlin.stdlib"); }

    }

    public static class KotlinxLibraryAccessors extends SubDependencyFactory {
        private final KotlinxCoroutinesLibraryAccessors laccForKotlinxCoroutinesLibraryAccessors = new KotlinxCoroutinesLibraryAccessors(owner);
        private final KotlinxSerializationLibraryAccessors laccForKotlinxSerializationLibraryAccessors = new KotlinxSerializationLibraryAccessors(owner);

        public KotlinxLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for datetime (org.jetbrains.kotlinx:kotlinx-datetime)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getDatetime() { return create("kotlinx.datetime"); }

        /**
         * Returns the group of libraries at kotlinx.coroutines
         */
        public KotlinxCoroutinesLibraryAccessors getCoroutines() { return laccForKotlinxCoroutinesLibraryAccessors; }

        /**
         * Returns the group of libraries at kotlinx.serialization
         */
        public KotlinxSerializationLibraryAccessors getSerialization() { return laccForKotlinxSerializationLibraryAccessors; }

    }

    public static class KotlinxCoroutinesLibraryAccessors extends SubDependencyFactory {

        public KotlinxCoroutinesLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for android (org.jetbrains.kotlinx:kotlinx-coroutines-android)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getAndroid() { return create("kotlinx.coroutines.android"); }

            /**
             * Creates a dependency provider for core (org.jetbrains.kotlinx:kotlinx-coroutines-core)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getCore() { return create("kotlinx.coroutines.core"); }

            /**
             * Creates a dependency provider for test (org.jetbrains.kotlinx:kotlinx-coroutines-test)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getTest() { return create("kotlinx.coroutines.test"); }

    }

    public static class KotlinxSerializationLibraryAccessors extends SubDependencyFactory {

        public KotlinxSerializationLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for json (org.jetbrains.kotlinx:kotlinx-serialization-json)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getJson() { return create("kotlinx.serialization.json"); }

    }

    public static class LintLibraryAccessors extends SubDependencyFactory {

        public LintLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for api (com.android.tools.lint:lint-api)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getApi() { return create("lint.api"); }

    }

    public static class MemfaultLibraryAccessors extends SubDependencyFactory {

        public MemfaultLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for cloud (com.memfault.cloud:cloud-android)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getCloud() { return create("memfault.cloud"); }

    }

    public static class NordicLibraryAccessors extends SubDependencyFactory {
        private final NordicBleLibraryAccessors laccForNordicBleLibraryAccessors = new NordicBleLibraryAccessors(owner);
        private final NordicLogLibraryAccessors laccForNordicLogLibraryAccessors = new NordicLogLibraryAccessors(owner);
        private final NordicMcumgrLibraryAccessors laccForNordicMcumgrLibraryAccessors = new NordicMcumgrLibraryAccessors(owner);

        public NordicLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for analytics (no.nordicsemi.android.common:analytics)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getAnalytics() { return create("nordic.analytics"); }

            /**
             * Creates a dependency provider for core (no.nordicsemi.android.common:core)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getCore() { return create("nordic.core"); }

            /**
             * Creates a dependency provider for dfu (no.nordicsemi.android:dfu)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getDfu() { return create("nordic.dfu"); }

            /**
             * Creates a dependency provider for memfault (no.nordicsemi.android:memfault)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getMemfault() { return create("nordic.memfault"); }

            /**
             * Creates a dependency provider for navigation (no.nordicsemi.android.common:navigation)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getNavigation() { return create("nordic.navigation"); }

            /**
             * Creates a dependency provider for permission (no.nordicsemi.android.common:permission)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getPermission() { return create("nordic.permission"); }

            /**
             * Creates a dependency provider for scanner (no.nordicsemi.android.support.v18:scanner)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getScanner() { return create("nordic.scanner"); }

            /**
             * Creates a dependency provider for theme (no.nordicsemi.android.common:theme)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getTheme() { return create("nordic.theme"); }

            /**
             * Creates a dependency provider for uilogger (no.nordicsemi.android.common:uilogger)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getUilogger() { return create("nordic.uilogger"); }

            /**
             * Creates a dependency provider for uiscanner (no.nordicsemi.android.common:uiscanner)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getUiscanner() { return create("nordic.uiscanner"); }

        /**
         * Returns the group of libraries at nordic.ble
         */
        public NordicBleLibraryAccessors getBle() { return laccForNordicBleLibraryAccessors; }

        /**
         * Returns the group of libraries at nordic.log
         */
        public NordicLogLibraryAccessors getLog() { return laccForNordicLogLibraryAccessors; }

        /**
         * Returns the group of libraries at nordic.mcumgr
         */
        public NordicMcumgrLibraryAccessors getMcumgr() { return laccForNordicMcumgrLibraryAccessors; }

    }

    public static class NordicBleLibraryAccessors extends SubDependencyFactory {

        public NordicBleLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for common (no.nordicsemi.android:ble-common)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getCommon() { return create("nordic.ble.common"); }

            /**
             * Creates a dependency provider for ktx (no.nordicsemi.android:ble-ktx)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getKtx() { return create("nordic.ble.ktx"); }

    }

    public static class NordicLogLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public NordicLogLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for log (no.nordicsemi.android:log)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> asProvider() { return create("nordic.log"); }

            /**
             * Creates a dependency provider for timber (no.nordicsemi.android:log-timber)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getTimber() { return create("nordic.log.timber"); }

    }

    public static class NordicMcumgrLibraryAccessors extends SubDependencyFactory {

        public NordicMcumgrLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for ble (no.nordicsemi.android:mcumgr-ble)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getBle() { return create("nordic.mcumgr.ble"); }

            /**
             * Creates a dependency provider for core (no.nordicsemi.android:mcumgr-core)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getCore() { return create("nordic.mcumgr.core"); }

    }

    public static class OkhttpLibraryAccessors extends SubDependencyFactory {

        public OkhttpLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for logging (com.squareup.okhttp3:logging-interceptor)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getLogging() { return create("okhttp.logging"); }

    }

    public static class ProtobufLibraryAccessors extends SubDependencyFactory {
        private final ProtobufKotlinLibraryAccessors laccForProtobufKotlinLibraryAccessors = new ProtobufKotlinLibraryAccessors(owner);

        public ProtobufLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for protoc (com.google.protobuf:protoc)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getProtoc() { return create("protobuf.protoc"); }

        /**
         * Returns the group of libraries at protobuf.kotlin
         */
        public ProtobufKotlinLibraryAccessors getKotlin() { return laccForProtobufKotlinLibraryAccessors; }

    }

    public static class ProtobufKotlinLibraryAccessors extends SubDependencyFactory {

        public ProtobufKotlinLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for lite (com.google.protobuf:protobuf-kotlin-lite)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getLite() { return create("protobuf.kotlin.lite"); }

    }

    public static class RetrofitLibraryAccessors extends SubDependencyFactory {
        private final RetrofitKotlinLibraryAccessors laccForRetrofitKotlinLibraryAccessors = new RetrofitKotlinLibraryAccessors(owner);

        public RetrofitLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for core (com.squareup.retrofit2:retrofit)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getCore() { return create("retrofit.core"); }

        /**
         * Returns the group of libraries at retrofit.kotlin
         */
        public RetrofitKotlinLibraryAccessors getKotlin() { return laccForRetrofitKotlinLibraryAccessors; }

    }

    public static class RetrofitKotlinLibraryAccessors extends SubDependencyFactory {

        public RetrofitKotlinLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for serialization (com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getSerialization() { return create("retrofit.kotlin.serialization"); }

    }

    public static class RoomLibraryAccessors extends SubDependencyFactory {

        public RoomLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for compiler (androidx.room:room-compiler)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getCompiler() { return create("room.compiler"); }

            /**
             * Creates a dependency provider for ktx (androidx.room:room-ktx)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getKtx() { return create("room.ktx"); }

            /**
             * Creates a dependency provider for runtime (androidx.room:room-runtime)
             * This dependency was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<MinimalExternalModuleDependency> getRuntime() { return create("room.runtime"); }

    }

    public static class VersionAccessors extends VersionFactory  {

        private final MemfaultVersionAccessors vaccForMemfaultVersionAccessors = new MemfaultVersionAccessors(providers, config);
        private final NordicVersionAccessors vaccForNordicVersionAccessors = new NordicVersionAccessors(providers, config);
        public VersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: accompanist (0.28.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getAccompanist() { return getVersion("accompanist"); }

            /**
             * Returns the version associated to this alias: androidDesugarJdkLibs (1.2.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getAndroidDesugarJdkLibs() { return getVersion("androidDesugarJdkLibs"); }

            /**
             * Returns the version associated to this alias: androidGradlePlugin (7.4.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getAndroidGradlePlugin() { return getVersion("androidGradlePlugin"); }

            /**
             * Returns the version associated to this alias: androidxActivity (1.6.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getAndroidxActivity() { return getVersion("androidxActivity"); }

            /**
             * Returns the version associated to this alias: androidxAnnotation (1.6.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getAndroidxAnnotation() { return getVersion("androidxAnnotation"); }

            /**
             * Returns the version associated to this alias: androidxAppCompat (1.6.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getAndroidxAppCompat() { return getVersion("androidxAppCompat"); }

            /**
             * Returns the version associated to this alias: androidxComposeBom (2023.01.00)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getAndroidxComposeBom() { return getVersion("androidxComposeBom"); }

            /**
             * Returns the version associated to this alias: androidxComposeCompiler (1.4.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getAndroidxComposeCompiler() { return getVersion("androidxComposeCompiler"); }

            /**
             * Returns the version associated to this alias: androidxComposeRuntimeTracing (1.0.0-alpha03)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getAndroidxComposeRuntimeTracing() { return getVersion("androidxComposeRuntimeTracing"); }

            /**
             * Returns the version associated to this alias: androidxCore (1.9.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getAndroidxCore() { return getVersion("androidxCore"); }

            /**
             * Returns the version associated to this alias: androidxCoreSplashscreen (1.0.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getAndroidxCoreSplashscreen() { return getVersion("androidxCoreSplashscreen"); }

            /**
             * Returns the version associated to this alias: androidxDataStore (1.0.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getAndroidxDataStore() { return getVersion("androidxDataStore"); }

            /**
             * Returns the version associated to this alias: androidxEspresso (3.5.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getAndroidxEspresso() { return getVersion("androidxEspresso"); }

            /**
             * Returns the version associated to this alias: androidxHiltNavigationCompose (1.0.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getAndroidxHiltNavigationCompose() { return getVersion("androidxHiltNavigationCompose"); }

            /**
             * Returns the version associated to this alias: androidxLifecycle (2.6.0-rc01)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getAndroidxLifecycle() { return getVersion("androidxLifecycle"); }

            /**
             * Returns the version associated to this alias: androidxLocalbroadcastmanager (1.1.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getAndroidxLocalbroadcastmanager() { return getVersion("androidxLocalbroadcastmanager"); }

            /**
             * Returns the version associated to this alias: androidxMacroBenchmark (1.1.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getAndroidxMacroBenchmark() { return getVersion("androidxMacroBenchmark"); }

            /**
             * Returns the version associated to this alias: androidxMetrics (1.0.0-alpha03)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getAndroidxMetrics() { return getVersion("androidxMetrics"); }

            /**
             * Returns the version associated to this alias: androidxNavigation (2.5.3)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getAndroidxNavigation() { return getVersion("androidxNavigation"); }

            /**
             * Returns the version associated to this alias: androidxProfileinstaller (1.2.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getAndroidxProfileinstaller() { return getVersion("androidxProfileinstaller"); }

            /**
             * Returns the version associated to this alias: androidxStartup (1.1.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getAndroidxStartup() { return getVersion("androidxStartup"); }

            /**
             * Returns the version associated to this alias: androidxTestCore (1.5.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getAndroidxTestCore() { return getVersion("androidxTestCore"); }

            /**
             * Returns the version associated to this alias: androidxTestExt (1.1.5)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getAndroidxTestExt() { return getVersion("androidxTestExt"); }

            /**
             * Returns the version associated to this alias: androidxTestRules (1.5.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getAndroidxTestRules() { return getVersion("androidxTestRules"); }

            /**
             * Returns the version associated to this alias: androidxTestRunner (1.5.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getAndroidxTestRunner() { return getVersion("androidxTestRunner"); }

            /**
             * Returns the version associated to this alias: androidxTracing (1.1.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getAndroidxTracing() { return getVersion("androidxTracing"); }

            /**
             * Returns the version associated to this alias: androidxUiAutomator (2.2.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getAndroidxUiAutomator() { return getVersion("androidxUiAutomator"); }

            /**
             * Returns the version associated to this alias: androidxWindowManager (1.0.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getAndroidxWindowManager() { return getVersion("androidxWindowManager"); }

            /**
             * Returns the version associated to this alias: androidxWork (2.8.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getAndroidxWork() { return getVersion("androidxWork"); }

            /**
             * Returns the version associated to this alias: chart (v3.1.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getChart() { return getVersion("chart"); }

            /**
             * Returns the version associated to this alias: coil (2.2.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getCoil() { return getVersion("coil"); }

            /**
             * Returns the version associated to this alias: firebaseBom (31.2.3)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getFirebaseBom() { return getVersion("firebaseBom"); }

            /**
             * Returns the version associated to this alias: firebaseCrashlyticsPlugins (2.9.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getFirebaseCrashlyticsPlugins() { return getVersion("firebaseCrashlyticsPlugins"); }

            /**
             * Returns the version associated to this alias: googleServicesPlugins (4.3.15)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getGoogleServicesPlugins() { return getVersion("googleServicesPlugins"); }

            /**
             * Returns the version associated to this alias: hilt (2.45)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getHilt() { return getVersion("hilt"); }

            /**
             * Returns the version associated to this alias: hiltExt (1.0.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getHiltExt() { return getVersion("hiltExt"); }

            /**
             * Returns the version associated to this alias: jacoco (0.8.7)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getJacoco() { return getVersion("jacoco"); }

            /**
             * Returns the version associated to this alias: junit4 (4.13.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getJunit4() { return getVersion("junit4"); }

            /**
             * Returns the version associated to this alias: kotlin (1.8.10)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getKotlin() { return getVersion("kotlin"); }

            /**
             * Returns the version associated to this alias: kotlinxCoroutines (1.6.4)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getKotlinxCoroutines() { return getVersion("kotlinxCoroutines"); }

            /**
             * Returns the version associated to this alias: kotlinxDatetime (0.4.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getKotlinxDatetime() { return getVersion("kotlinxDatetime"); }

            /**
             * Returns the version associated to this alias: kotlinxSerializationJson (1.5.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getKotlinxSerializationJson() { return getVersion("kotlinxSerializationJson"); }

            /**
             * Returns the version associated to this alias: ksp (1.8.10-1.0.9)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getKsp() { return getVersion("ksp"); }

            /**
             * Returns the version associated to this alias: leakcanary (2.10)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getLeakcanary() { return getVersion("leakcanary"); }

            /**
             * Returns the version associated to this alias: lint (30.4.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getLint() { return getVersion("lint"); }

            /**
             * Returns the version associated to this alias: markdown (0.3.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getMarkdown() { return getVersion("markdown"); }

            /**
             * Returns the version associated to this alias: nordicPlugins (1.3.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getNordicPlugins() { return getVersion("nordicPlugins"); }

            /**
             * Returns the version associated to this alias: okhttp (4.10.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getOkhttp() { return getVersion("okhttp"); }

            /**
             * Returns the version associated to this alias: protobuf (3.22.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getProtobuf() { return getVersion("protobuf"); }

            /**
             * Returns the version associated to this alias: protobufPlugin (0.9.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getProtobufPlugin() { return getVersion("protobufPlugin"); }

            /**
             * Returns the version associated to this alias: publishPlugin (1.1.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getPublishPlugin() { return getVersion("publishPlugin"); }

            /**
             * Returns the version associated to this alias: retrofit (2.9.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getRetrofit() { return getVersion("retrofit"); }

            /**
             * Returns the version associated to this alias: retrofitKotlinxSerializationJson (0.8.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getRetrofitKotlinxSerializationJson() { return getVersion("retrofitKotlinxSerializationJson"); }

            /**
             * Returns the version associated to this alias: room (2.5.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getRoom() { return getVersion("room"); }

            /**
             * Returns the version associated to this alias: secrets (2.0.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getSecrets() { return getVersion("secrets"); }

            /**
             * Returns the version associated to this alias: timber (5.0.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getTimber() { return getVersion("timber"); }

            /**
             * Returns the version associated to this alias: turbine (0.12.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getTurbine() { return getVersion("turbine"); }

            /**
             * Returns the version associated to this alias: wirePlugin (4.5.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getWirePlugin() { return getVersion("wirePlugin"); }

        /**
         * Returns the group of versions at versions.memfault
         */
        public MemfaultVersionAccessors getMemfault() { return vaccForMemfaultVersionAccessors; }

        /**
         * Returns the group of versions at versions.nordic
         */
        public NordicVersionAccessors getNordic() { return vaccForNordicVersionAccessors; }

    }

    public static class MemfaultVersionAccessors extends VersionFactory  {

        public MemfaultVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: memfault.cloud (2.0.4)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getCloud() { return getVersion("memfault.cloud"); }

    }

    public static class NordicVersionAccessors extends VersionFactory  {

        public NordicVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: nordic.ble (2.6.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getBle() { return getVersion("nordic.ble"); }

            /**
             * Returns the version associated to this alias: nordic.common (1.4.3)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getCommon() { return getVersion("nordic.common"); }

            /**
             * Returns the version associated to this alias: nordic.dfu (2.3.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getDfu() { return getVersion("nordic.dfu"); }

            /**
             * Returns the version associated to this alias: nordic.log (2.3.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getLog() { return getVersion("nordic.log"); }

            /**
             * Returns the version associated to this alias: nordic.mcumgr (1.6.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getMcumgr() { return getVersion("nordic.mcumgr"); }

            /**
             * Returns the version associated to this alias: nordic.memfault (1.0.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getMemfault() { return getVersion("nordic.memfault"); }

            /**
             * Returns the version associated to this alias: nordic.scanner (1.6.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<String> getScanner() { return getVersion("nordic.scanner"); }

    }

    public static class BundleAccessors extends BundleFactory {

        public BundleAccessors(ObjectFactory objects, ProviderFactory providers, DefaultVersionCatalog config, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) { super(objects, providers, config, attributesFactory, capabilityNotationParser); }

    }

    public static class PluginAccessors extends PluginFactory {
        private final AndroidPluginAccessors paccForAndroidPluginAccessors = new AndroidPluginAccessors(providers, config);
        private final FirebasePluginAccessors paccForFirebasePluginAccessors = new FirebasePluginAccessors(providers, config);
        private final GooglePluginAccessors paccForGooglePluginAccessors = new GooglePluginAccessors(providers, config);
        private final KotlinPluginAccessors paccForKotlinPluginAccessors = new KotlinPluginAccessors(providers, config);
        private final NordicPluginAccessors paccForNordicPluginAccessors = new NordicPluginAccessors(providers, config);

        public PluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Creates a plugin provider for hilt to the plugin id 'com.google.dagger.hilt.android'
             * This plugin was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<PluginDependency> getHilt() { return createPlugin("hilt"); }

            /**
             * Creates a plugin provider for ksp to the plugin id 'com.google.devtools.ksp'
             * This plugin was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<PluginDependency> getKsp() { return createPlugin("ksp"); }

            /**
             * Creates a plugin provider for protobuf to the plugin id 'com.google.protobuf'
             * This plugin was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<PluginDependency> getProtobuf() { return createPlugin("protobuf"); }

            /**
             * Creates a plugin provider for publish to the plugin id 'com.gradle.plugin-publish'
             * This plugin was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<PluginDependency> getPublish() { return createPlugin("publish"); }

            /**
             * Creates a plugin provider for secrets to the plugin id 'com.google.android.libraries.mapsplatform.secrets-gradle-plugin'
             * This plugin was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<PluginDependency> getSecrets() { return createPlugin("secrets"); }

            /**
             * Creates a plugin provider for wire to the plugin id 'com.squareup.wire'
             * This plugin was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<PluginDependency> getWire() { return createPlugin("wire"); }

        /**
         * Returns the group of plugins at plugins.android
         */
        public AndroidPluginAccessors getAndroid() { return paccForAndroidPluginAccessors; }

        /**
         * Returns the group of plugins at plugins.firebase
         */
        public FirebasePluginAccessors getFirebase() { return paccForFirebasePluginAccessors; }

        /**
         * Returns the group of plugins at plugins.google
         */
        public GooglePluginAccessors getGoogle() { return paccForGooglePluginAccessors; }

        /**
         * Returns the group of plugins at plugins.kotlin
         */
        public KotlinPluginAccessors getKotlin() { return paccForKotlinPluginAccessors; }

        /**
         * Returns the group of plugins at plugins.nordic
         */
        public NordicPluginAccessors getNordic() { return paccForNordicPluginAccessors; }

    }

    public static class AndroidPluginAccessors extends PluginFactory {

        public AndroidPluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Creates a plugin provider for android.application to the plugin id 'com.android.application'
             * This plugin was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<PluginDependency> getApplication() { return createPlugin("android.application"); }

            /**
             * Creates a plugin provider for android.library to the plugin id 'com.android.library'
             * This plugin was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<PluginDependency> getLibrary() { return createPlugin("android.library"); }

            /**
             * Creates a plugin provider for android.test to the plugin id 'com.android.test'
             * This plugin was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<PluginDependency> getTest() { return createPlugin("android.test"); }

    }

    public static class FirebasePluginAccessors extends PluginFactory {

        public FirebasePluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Creates a plugin provider for firebase.crashlytics to the plugin id 'com.google.firebase.crashlytics'
             * This plugin was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<PluginDependency> getCrashlytics() { return createPlugin("firebase.crashlytics"); }

    }

    public static class GooglePluginAccessors extends PluginFactory {

        public GooglePluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Creates a plugin provider for google.services to the plugin id 'com.google.gms.google-services'
             * This plugin was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<PluginDependency> getServices() { return createPlugin("google.services"); }

    }

    public static class KotlinPluginAccessors extends PluginFactory {

        public KotlinPluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Creates a plugin provider for kotlin.android to the plugin id 'org.jetbrains.kotlin.android'
             * This plugin was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<PluginDependency> getAndroid() { return createPlugin("kotlin.android"); }

            /**
             * Creates a plugin provider for kotlin.jvm to the plugin id 'org.jetbrains.kotlin.jvm'
             * This plugin was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<PluginDependency> getJvm() { return createPlugin("kotlin.jvm"); }

            /**
             * Creates a plugin provider for kotlin.kapt to the plugin id 'org.jetbrains.kotlin.kapt'
             * This plugin was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<PluginDependency> getKapt() { return createPlugin("kotlin.kapt"); }

            /**
             * Creates a plugin provider for kotlin.parcelize to the plugin id 'org.jetbrains.kotlin.plugin.parcelize'
             * This plugin was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<PluginDependency> getParcelize() { return createPlugin("kotlin.parcelize"); }

            /**
             * Creates a plugin provider for kotlin.serialization to the plugin id 'org.jetbrains.kotlin.plugin.serialization'
             * This plugin was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<PluginDependency> getSerialization() { return createPlugin("kotlin.serialization"); }

    }

    public static class NordicPluginAccessors extends PluginFactory {
        private final NordicApplicationPluginAccessors paccForNordicApplicationPluginAccessors = new NordicApplicationPluginAccessors(providers, config);
        private final NordicLibraryPluginAccessors paccForNordicLibraryPluginAccessors = new NordicLibraryPluginAccessors(providers, config);

        public NordicPluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Creates a plugin provider for nordic.feature to the plugin id 'no.nordicsemi.android.gradle.feature'
             * This plugin was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<PluginDependency> getFeature() { return createPlugin("nordic.feature"); }

            /**
             * Creates a plugin provider for nordic.hilt to the plugin id 'no.nordicsemi.android.gradle.hilt'
             * This plugin was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<PluginDependency> getHilt() { return createPlugin("nordic.hilt"); }

            /**
             * Creates a plugin provider for nordic.kotlin to the plugin id 'no.nordicsemi.android.gradle.kotlin'
             * This plugin was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<PluginDependency> getKotlin() { return createPlugin("nordic.kotlin"); }

            /**
             * Creates a plugin provider for nordic.nexus to the plugin id 'no.nordicsemi.android.gradle.nexus'
             * This plugin was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<PluginDependency> getNexus() { return createPlugin("nordic.nexus"); }

        /**
         * Returns the group of plugins at plugins.nordic.application
         */
        public NordicApplicationPluginAccessors getApplication() { return paccForNordicApplicationPluginAccessors; }

        /**
         * Returns the group of plugins at plugins.nordic.library
         */
        public NordicLibraryPluginAccessors getLibrary() { return paccForNordicLibraryPluginAccessors; }

    }

    public static class NordicApplicationPluginAccessors extends PluginFactory  implements PluginNotationSupplier{

        public NordicApplicationPluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Creates a plugin provider for nordic.application to the plugin id 'no.nordicsemi.android.gradle.application'
             * This plugin was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<PluginDependency> asProvider() { return createPlugin("nordic.application"); }

            /**
             * Creates a plugin provider for nordic.application.compose to the plugin id 'no.nordicsemi.android.gradle.application.compose'
             * This plugin was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<PluginDependency> getCompose() { return createPlugin("nordic.application.compose"); }

    }

    public static class NordicLibraryPluginAccessors extends PluginFactory  implements PluginNotationSupplier{

        public NordicLibraryPluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Creates a plugin provider for nordic.library to the plugin id 'no.nordicsemi.android.gradle.library'
             * This plugin was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<PluginDependency> asProvider() { return createPlugin("nordic.library"); }

            /**
             * Creates a plugin provider for nordic.library.compose to the plugin id 'no.nordicsemi.android.gradle.library.compose'
             * This plugin was declared in catalog no.nordicsemi.android.gradle:version-catalog:1.3.3
             */
            public Provider<PluginDependency> getCompose() { return createPlugin("nordic.library.compose"); }

    }

}
