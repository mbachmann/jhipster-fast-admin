import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CostUnitFaService } from '../service/cost-unit-fa.service';

import { CostUnitFaComponent } from './cost-unit-fa.component';

describe('Component Tests', () => {
  describe('CostUnitFa Management Component', () => {
    let comp: CostUnitFaComponent;
    let fixture: ComponentFixture<CostUnitFaComponent>;
    let service: CostUnitFaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CostUnitFaComponent],
      })
        .overrideTemplate(CostUnitFaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CostUnitFaComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(CostUnitFaService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.costUnits?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
