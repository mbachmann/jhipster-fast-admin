import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CostUnitService } from '../service/cost-unit.service';

import { CostUnitComponent } from './cost-unit.component';

describe('Component Tests', () => {
  describe('CostUnit Management Component', () => {
    let comp: CostUnitComponent;
    let fixture: ComponentFixture<CostUnitComponent>;
    let service: CostUnitService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CostUnitComponent],
      })
        .overrideTemplate(CostUnitComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CostUnitComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(CostUnitService);

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
