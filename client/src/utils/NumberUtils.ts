/**
 * Converts the given number to the amount in cents (rounded)
 * @param input a floating point number.
 */
export function toCents(input: number): number {
    return Math.round(input * 100)
}