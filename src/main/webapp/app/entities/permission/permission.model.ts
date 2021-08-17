export interface IPermission {
  id?: number;
  name?: string | null;
}

export class Permission implements IPermission {
  constructor(public id?: number, public name?: string | null) {}
}

export function getPermissionIdentifier(permission: IPermission): number | undefined {
  return permission.id;
}
