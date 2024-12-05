
export function os2(): string {
    const platform = navigator.platform;
    if (platform.toLocaleLowerCase().startsWith("win")) {
        return "win";
    } else {
        return "posix";
    }
}
