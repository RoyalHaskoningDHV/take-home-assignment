/**
 * Not empty filter function to remove null or undefined values from an array (typed).
 */
export function notEmpty<TValue>(value: TValue | null | undefined): value is TValue {
    return value !== null && value !== undefined;
}