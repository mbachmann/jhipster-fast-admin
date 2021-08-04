import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DomainResource } from 'app/entities/enumerations/domain-resource.model';
import { IPermissionMySuffix, PermissionMySuffix } from '../permission-my-suffix.model';

import { PermissionMySuffixService } from './permission-my-suffix.service';

describe('Service Tests', () => {
  describe('PermissionMySuffix Service', () => {
    let service: PermissionMySuffixService;
    let httpMock: HttpTestingController;
    let elemDefault: IPermissionMySuffix;
    let expectedResult: IPermissionMySuffix | IPermissionMySuffix[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PermissionMySuffixService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        newAll: false,
        editOwn: false,
        editAll: false,
        viewOwn: false,
        viewAll: false,
        manageOwn: false,
        manageAll: false,
        domainResource: DomainResource.AFFILIATE,
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

      it('should create a PermissionMySuffix', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new PermissionMySuffix()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a PermissionMySuffix', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            newAll: true,
            editOwn: true,
            editAll: true,
            viewOwn: true,
            viewAll: true,
            manageOwn: true,
            manageAll: true,
            domainResource: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a PermissionMySuffix', () => {
        const patchObject = Object.assign(
          {
            newAll: true,
            editOwn: true,
            viewOwn: true,
            viewAll: true,
            manageAll: true,
          },
          new PermissionMySuffix()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of PermissionMySuffix', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            newAll: true,
            editOwn: true,
            editAll: true,
            viewOwn: true,
            viewAll: true,
            manageOwn: true,
            manageAll: true,
            domainResource: 'BBBBBB',
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

      it('should delete a PermissionMySuffix', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPermissionMySuffixToCollectionIfMissing', () => {
        it('should add a PermissionMySuffix to an empty array', () => {
          const permission: IPermissionMySuffix = { id: 123 };
          expectedResult = service.addPermissionMySuffixToCollectionIfMissing([], permission);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(permission);
        });

        it('should not add a PermissionMySuffix to an array that contains it', () => {
          const permission: IPermissionMySuffix = { id: 123 };
          const permissionCollection: IPermissionMySuffix[] = [
            {
              ...permission,
            },
            { id: 456 },
          ];
          expectedResult = service.addPermissionMySuffixToCollectionIfMissing(permissionCollection, permission);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a PermissionMySuffix to an array that doesn't contain it", () => {
          const permission: IPermissionMySuffix = { id: 123 };
          const permissionCollection: IPermissionMySuffix[] = [{ id: 456 }];
          expectedResult = service.addPermissionMySuffixToCollectionIfMissing(permissionCollection, permission);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(permission);
        });

        it('should add only unique PermissionMySuffix to an array', () => {
          const permissionArray: IPermissionMySuffix[] = [{ id: 123 }, { id: 456 }, { id: 47932 }];
          const permissionCollection: IPermissionMySuffix[] = [{ id: 123 }];
          expectedResult = service.addPermissionMySuffixToCollectionIfMissing(permissionCollection, ...permissionArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const permission: IPermissionMySuffix = { id: 123 };
          const permission2: IPermissionMySuffix = { id: 456 };
          expectedResult = service.addPermissionMySuffixToCollectionIfMissing([], permission, permission2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(permission);
          expect(expectedResult).toContain(permission2);
        });

        it('should accept null and undefined values', () => {
          const permission: IPermissionMySuffix = { id: 123 };
          expectedResult = service.addPermissionMySuffixToCollectionIfMissing([], null, permission, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(permission);
        });

        it('should return initial array if no PermissionMySuffix is added', () => {
          const permissionCollection: IPermissionMySuffix[] = [{ id: 123 }];
          expectedResult = service.addPermissionMySuffixToCollectionIfMissing(permissionCollection, undefined, null);
          expect(expectedResult).toEqual(permissionCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
