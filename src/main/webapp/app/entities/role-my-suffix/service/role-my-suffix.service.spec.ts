import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRoleMySuffix, RoleMySuffix } from '../role-my-suffix.model';

import { RoleMySuffixService } from './role-my-suffix.service';

describe('Service Tests', () => {
  describe('RoleMySuffix Service', () => {
    let service: RoleMySuffixService;
    let httpMock: HttpTestingController;
    let elemDefault: IRoleMySuffix;
    let expectedResult: IRoleMySuffix | IRoleMySuffix[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(RoleMySuffixService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
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

      it('should create a RoleMySuffix', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new RoleMySuffix()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a RoleMySuffix', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a RoleMySuffix', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
          },
          new RoleMySuffix()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of RoleMySuffix', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
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

      it('should delete a RoleMySuffix', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addRoleMySuffixToCollectionIfMissing', () => {
        it('should add a RoleMySuffix to an empty array', () => {
          const role: IRoleMySuffix = { id: 123 };
          expectedResult = service.addRoleMySuffixToCollectionIfMissing([], role);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(role);
        });

        it('should not add a RoleMySuffix to an array that contains it', () => {
          const role: IRoleMySuffix = { id: 123 };
          const roleCollection: IRoleMySuffix[] = [
            {
              ...role,
            },
            { id: 456 },
          ];
          expectedResult = service.addRoleMySuffixToCollectionIfMissing(roleCollection, role);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a RoleMySuffix to an array that doesn't contain it", () => {
          const role: IRoleMySuffix = { id: 123 };
          const roleCollection: IRoleMySuffix[] = [{ id: 456 }];
          expectedResult = service.addRoleMySuffixToCollectionIfMissing(roleCollection, role);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(role);
        });

        it('should add only unique RoleMySuffix to an array', () => {
          const roleArray: IRoleMySuffix[] = [{ id: 123 }, { id: 456 }, { id: 97391 }];
          const roleCollection: IRoleMySuffix[] = [{ id: 123 }];
          expectedResult = service.addRoleMySuffixToCollectionIfMissing(roleCollection, ...roleArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const role: IRoleMySuffix = { id: 123 };
          const role2: IRoleMySuffix = { id: 456 };
          expectedResult = service.addRoleMySuffixToCollectionIfMissing([], role, role2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(role);
          expect(expectedResult).toContain(role2);
        });

        it('should accept null and undefined values', () => {
          const role: IRoleMySuffix = { id: 123 };
          expectedResult = service.addRoleMySuffixToCollectionIfMissing([], null, role, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(role);
        });

        it('should return initial array if no RoleMySuffix is added', () => {
          const roleCollection: IRoleMySuffix[] = [{ id: 123 }];
          expectedResult = service.addRoleMySuffixToCollectionIfMissing(roleCollection, undefined, null);
          expect(expectedResult).toEqual(roleCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
