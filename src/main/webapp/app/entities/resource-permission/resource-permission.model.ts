import { IApplicationRole } from 'app/entities/application-role/application-role.model';
import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { PermissionType } from 'app/entities/enumerations/permission-type.model';
import { DomainArea } from 'app/entities/enumerations/domain-area.model';

export interface IResourcePermission {
  id?: number;
  add?: PermissionType;
  edit?: PermissionType;
  manage?: PermissionType;
  domainArea?: DomainArea;
  role?: IApplicationRole | null;
  applicationUser?: IApplicationUser | null;
}

export class ResourcePermission implements IResourcePermission {
  constructor(
    public id?: number,
    public add?: PermissionType,
    public edit?: PermissionType,
    public manage?: PermissionType,
    public domainArea?: DomainArea,
    public role?: IApplicationRole | null,
    public applicationUser?: IApplicationUser | null
  ) {}
}

export function getResourcePermissionIdentifier(resourcePermission: IResourcePermission): number | undefined {
  return resourcePermission.id;
}
