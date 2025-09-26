package pt.rafap.clilib.datastore

import pt.rafap.clilib.datatypes.Operator
import pt.rafap.clilib.tools.tExt.doOperation

object Operators {

    class ADD: Operator<Any> {
        override fun invoke(a: Any?, b: Any?): Any? {
            // If this type has a plus operator defined, use it
            if (a == null || b == null) {
                throw IllegalArgumentException("Cannot add null values")
            }
            return try {
                a.doOperation(b, "plus")
            } catch (e: Exception) {
                throw IllegalArgumentException("Error performing addition: ${e.message}", e)
            }
        }
    }

    class SUB: Operator<Any> {
        override fun invoke(a: Any?, b: Any?): Any? {
            if (a == null || b == null) {
                throw IllegalArgumentException("Cannot subtract null values")
            }
            return try {
                a.doOperation(b, "minus")
            } catch (e: Exception) {
                throw IllegalArgumentException("Error performing subtraction: ${e.message}", e)
            }
        }
    }

    class MUL: Operator<Any> {
        override fun invoke(a: Any?, b: Any?): Any? {
            if (a == null || b == null) {
                throw IllegalArgumentException("Cannot multiply null values")
            }
            return try {
                a.doOperation(b, "times")
            } catch (e: Exception) {
                throw IllegalArgumentException("Error performing multiplication: ${e.message}", e)
            }
        }
    }

    class DIV: Operator<Any> {
        override fun invoke(a: Any?, b: Any?): Any? {
            if (a == null || b == null) {
                throw IllegalArgumentException("Cannot divide null values")
            }
            if (b == 0 || b == 0.0) {
                throw IllegalArgumentException("Cannot divide by zero")
            }
            return try {
                a.doOperation(b, "div")
            } catch (e: Exception) {
                throw IllegalArgumentException("Error performing division: ${e.message}", e)
            }
        }
    }

    class MOD: Operator<Any> {
        override fun invoke(a: Any?, b: Any?): Any? {
            if (a == null || b == null) {
                throw IllegalArgumentException("Cannot perform modulo with null values")
            }
            if (b == 0 || b == 0.0) {
                throw IllegalArgumentException("Cannot perform modulo by zero")
            }
            return try {
                a.doOperation(b, "mod")
            } catch (e: Exception) {
                throw IllegalArgumentException("Error performing modulo operation: ${e.message}", e)
            }
        }
    }

    class POW: Operator<Any> {
        override fun invoke(a: Any?, b: Any?): Any? {
            if (a == null || b == null) {
                throw IllegalArgumentException("Cannot perform power operation with null values")
            }
            return try {
                a.doOperation(b, "pow")
            } catch (e: Exception) {
                throw IllegalArgumentException("Error performing power operation: ${e.message}", e)
            }
        }
    }
}