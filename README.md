# FeatureFlagExample
One of the key ingredients to speed up modern software development is using feature flags. However, they can still be quite a drag to integrate into your app as existing feature flag frameworks mostly focus on the remote toggling aspect of feature flags.

This repository shows a clear, very easy to use architecture to manage feature flags. The benefits are:

- Super easy to add feature flags: just a single LOC
- Support for local and remote feature flags
- Support for both feature flags (temporary for development) and test settings (indefintely to facilitate testing)
- Automatically generated UI to toggle feature flags in debug builds
- Able to use in unit/instrumentation testing
- Support any remote feature flag tool

Read more about it in this blogpost: https://jeroenmols.com/blog/2019/08/27/featureflagsarchitecture/

## Architecture

![Feature flag architecture](https://raw.githubusercontent.com/JeroenMols/FeatureFlagExample/master/readme/featureflag_architecture.png)

## Test settings
In app activity to toggle feature flags and test settings locally on or off. This dramatically helps with testing.

![Feature flag test settings](https://raw.githubusercontent.com/JeroenMols/FeatureFlagExample/master/readme/test_settings.jpg)
