import { jwtDecode } from "jwt-decode";

export function getUuidUserFromToken(token: string): string | null {
    try {
      const decoded: any = jwtDecode(token);
      console.log(token);
      return decoded?.uuid || null;
    } catch (error) {
      console.error('Error decoding token', error);
      return null;
    }
  }
