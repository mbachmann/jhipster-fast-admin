import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { PermissionType } from 'app/entities/enumerations/permission-type.model';
import { DomainArea } from 'app/entities/enumerations/domain-area.model';
import { IPermissionFa, PermissionFa } from '../permission-fa.model';

import { PermissionFaService } from './permission-fa.service';

describe('Service Tests', () => {
  describe('PermissionFa Service', () => {
    let service: PermissionFaService;
    let httpMock: HttpTestingController;
    let elemDefault: IPermissionFa;
    let expectedResult: IPermissionFa | IPermissionFa[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PermissionFaService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        add: PermissionType.NONE,
        edit: PermissionType.NONE,
        manage: PermissionType.NONE,
        domainArea: DomainArea.AFFILIATE,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a PermissionFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new PermissionFa()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a PermissionFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            add: 'BBBBBB',
            edit: 'BBBBBB',
            manage: 'BBBBBB',
            domainArea: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a PermissionFa', () => {
        const patchObject = Object.assign(
          {
            add: 'BBBBBB',
            edit: 'BBBBBB',
            domainArea: 'BBBBBB',
          },
          new PermissionFa()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of PermissionFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            add: 'BBBBBB',
            edit: 'BBBBBB',
            manage: 'BBBBBB',
            domainArea: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a PermissionFa', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPermissionFaToCollectionIfMissing', () => {
        it('should add a PermissionFa to an empty array', () => {
          const permission: IPermissionFa = { id: 123 };
          expectedResult = service.addPermissionFaToCollectionIfMissing([], permission);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(permission);
        });

        it('should not add a PermissionFa to an array that contains it', () => {
          const permission: IPermissionFa = { id: 123 };
          const permissionCollection: IPermissionFa[] = [
            {
              ...permission,
            },
            { id: 456 },
          ];
          expectedResult = service.addPermissionFaToCollectionIfMissing(permissionCollection, permission);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a PermissionFa to an array that doesn't contain it", () => {
          const permission: IPermissionFa = { id: 123 };
          const permissionCollection: IPermissionFa[] = [{ id: 456 }];
          expectedResult = service.addPermissionFaToCollectionIfMissing(permissionCollection, permission);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(permission);
        });

        it('should add only unique PermissionFa to an array', () => {
          const permissionArray: IPermissionFa[] = [{ id: 123 }, { id: 456 }, { id: 89332 }];
          const permissionCollection: IPermissionFa[] = [{ id: 123 }];
          expectedResult = service.addPermissionFaToCollectionIfMissing(permissionCollection, ...permissionArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const permission: IPermissionFa = { id: 123 };
          const permission2: IPermissionFa = { id: 456 };
          expectedResult = service.addPermissionFaToCollectionIfMissing([], permission, permission2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(permission);
          expect(expectedResult).toContain(permission2);
        });

        it('should accept null and undefined values', () => {
          const permission: IPermissionFa = { id: 123 };
          expectedResult = service.addPermissionFaToCollectionIfMissing([], null, permission, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(permission);
        });

        it('should return initial array if no PermissionFa is added', () => {
          const permissionCollection: IPermissionFa[] = [{ id: 123 }];
          expectedResult = service.addPermissionFaToCollectionIfMissing(permissionCollection, undefined, null);
          expect(expectedResult).toEqual(permissionCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
