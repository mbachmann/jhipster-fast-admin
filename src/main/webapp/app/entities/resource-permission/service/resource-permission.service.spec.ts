import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { PermissionType } from 'app/entities/enumerations/permission-type.model';
import { DomainArea } from 'app/entities/enumerations/domain-area.model';
import { IResourcePermission, ResourcePermission } from '../resource-permission.model';

import { ResourcePermissionService } from './resource-permission.service';

describe('Service Tests', () => {
  describe('ResourcePermission Service', () => {
    let service: ResourcePermissionService;
    let httpMock: HttpTestingController;
    let elemDefault: IResourcePermission;
    let expectedResult: IResourcePermission | IResourcePermission[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ResourcePermissionService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        add: PermissionType.NONE,
        edit: PermissionType.NONE,
        manage: PermissionType.NONE,
        domainArea: DomainArea.CONTACTS,
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

      it('should create a ResourcePermission', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ResourcePermission()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ResourcePermission', () => {
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

      it('should partial update a ResourcePermission', () => {
        const patchObject = Object.assign(
          {
            edit: 'BBBBBB',
            domainArea: 'BBBBBB',
          },
          new ResourcePermission()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ResourcePermission', () => {
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

      it('should delete a ResourcePermission', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addResourcePermissionToCollectionIfMissing', () => {
        it('should add a ResourcePermission to an empty array', () => {
          const resourcePermission: IResourcePermission = { id: 123 };
          expectedResult = service.addResourcePermissionToCollectionIfMissing([], resourcePermission);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(resourcePermission);
        });

        it('should not add a ResourcePermission to an array that contains it', () => {
          const resourcePermission: IResourcePermission = { id: 123 };
          const resourcePermissionCollection: IResourcePermission[] = [
            {
              ...resourcePermission,
            },
            { id: 456 },
          ];
          expectedResult = service.addResourcePermissionToCollectionIfMissing(resourcePermissionCollection, resourcePermission);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ResourcePermission to an array that doesn't contain it", () => {
          const resourcePermission: IResourcePermission = { id: 123 };
          const resourcePermissionCollection: IResourcePermission[] = [{ id: 456 }];
          expectedResult = service.addResourcePermissionToCollectionIfMissing(resourcePermissionCollection, resourcePermission);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(resourcePermission);
        });

        it('should add only unique ResourcePermission to an array', () => {
          const resourcePermissionArray: IResourcePermission[] = [{ id: 123 }, { id: 456 }, { id: 98591 }];
          const resourcePermissionCollection: IResourcePermission[] = [{ id: 123 }];
          expectedResult = service.addResourcePermissionToCollectionIfMissing(resourcePermissionCollection, ...resourcePermissionArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const resourcePermission: IResourcePermission = { id: 123 };
          const resourcePermission2: IResourcePermission = { id: 456 };
          expectedResult = service.addResourcePermissionToCollectionIfMissing([], resourcePermission, resourcePermission2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(resourcePermission);
          expect(expectedResult).toContain(resourcePermission2);
        });

        it('should accept null and undefined values', () => {
          const resourcePermission: IResourcePermission = { id: 123 };
          expectedResult = service.addResourcePermissionToCollectionIfMissing([], null, resourcePermission, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(resourcePermission);
        });

        it('should return initial array if no ResourcePermission is added', () => {
          const resourcePermissionCollection: IResourcePermission[] = [{ id: 123 }];
          expectedResult = service.addResourcePermissionToCollectionIfMissing(resourcePermissionCollection, undefined, null);
          expect(expectedResult).toEqual(resourcePermissionCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
