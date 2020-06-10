package pl.karol202.uranium.core.common

/**
 * Object that can be used as component's key and should be always used as default key
 * if no other key is specified. The component dispatching algorithm should
 * automatically determine identity between components with [AutoKey] used as key.
 *
 * Currently (0.2.3 version) parent component can specify only one child component using [AutoKey].
 * In case of using multiple components with [AutoKey] next to each other,
 * an error is reported by the dispatching algorithm.
 * This is a critical issue, thus expected to be fixed soon. See issue #1
 */
object AutoKey

/**
 * Interface for classes that have a key (components, elements, etc.)
 */
interface UKeyProvider
{
	/**
	 * Provided key, can be of any type
	 */
	val key: Any
}
