import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRoleFa, RoleFa } from '../role-fa.model';

import { RoleFaService } from './role-fa.service';

describe('Service Tests', () => {
  describe('RoleFa Service', () => {
    let service: RoleFaService;
    let httpMock: HttpTestingController;
    let elemDefault: IRoleFa;
    let expectedResult: IRoleFa | IRoleFa[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(RoleFaService);
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

      it('should create a RoleFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new RoleFa()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a RoleFa', () => {
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

      it('should partial update a RoleFa', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
          },
          new RoleFa()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of RoleFa', () => {
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

      it('should delete a RoleFa', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addRoleFaToCollectionIfMissing', () => {
        it('should add a RoleFa to an empty array', () => {
          const role: IRoleFa = { id: 123 };
          expectedResult = service.addRoleFaToCollectionIfMissing([], role);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(role);
        });

        it('should not add a RoleFa to an array that contains it', () => {
          const role: IRoleFa = { id: 123 };
          const roleCollection: IRoleFa[] = [
            {
              ...role,
            },
            { id: 456 },
          ];
          expectedResult = service.addRoleFaToCollectionIfMissing(roleCollection, role);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a RoleFa to an array that doesn't contain it", () => {
          const role: IRoleFa = { id: 123 };
          const roleCollection: IRoleFa[] = [{ id: 456 }];
          expectedResult = service.addRoleFaToCollectionIfMissing(roleCollection, role);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(role);
        });

        it('should add only unique RoleFa to an array', () => {
          const roleArray: IRoleFa[] = [{ id: 123 }, { id: 456 }, { id: 97391 }];
          const roleCollection: IRoleFa[] = [{ id: 123 }];
          expectedResult = service.addRoleFaToCollectionIfMissing(roleCollection, ...roleArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const role: IRoleFa = { id: 123 };
          const role2: IRoleFa = { id: 456 };
          expectedResult = service.addRoleFaToCollectionIfMissing([], role, role2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(role);
          expect(expectedResult).toContain(role2);
        });

        it('should accept null and undefined values', () => {
          const role: IRoleFa = { id: 123 };
          expectedResult = service.addRoleFaToCollectionIfMissing([], null, role, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(role);
        });

        it('should return initial array if no RoleFa is added', () => {
          const roleCollection: IRoleFa[] = [{ id: 123 }];
          expectedResult = service.addRoleFaToCollectionIfMissing(roleCollection, undefined, null);
          expect(expectedResult).toEqual(roleCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
