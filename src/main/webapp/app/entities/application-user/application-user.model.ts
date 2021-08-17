import * as dayjs from 'dayjs';

export interface IApplicationUser {
  id?: number;
  shortCutName?: string;
  memberSince?: dayjs.Dayjs | null;
  avatarContentType?: string | null;
  avatar?: string | null;
  imageType?: string | null;
  inactiv?: boolean | null;
}

export class ApplicationUser implements IApplicationUser {
  constructor(
    public id?: number,
    public shortCutName?: string,
    public memberSince?: dayjs.Dayjs | null,
    public avatarContentType?: string | null,
    public avatar?: string | null,
    public imageType?: string | null,
    public inactiv?: boolean | null
  ) {
    this.inactiv = this.inactiv ?? false;
  }
}

export function getApplicationUserIdentifier(applicationUser: IApplicationUser): number | undefined {
  return applicationUser.id;
}
