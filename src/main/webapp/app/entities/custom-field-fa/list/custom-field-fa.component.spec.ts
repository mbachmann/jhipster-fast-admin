jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CustomFieldFaService } from '../service/custom-field-fa.service';

import { CustomFieldFaComponent } from './custom-field-fa.component';

describe('Component Tests', () => {
  describe('CustomFieldFa Management Component', () => {
    let comp: CustomFieldFaComponent;
    let fixture: ComponentFixture<CustomFieldFaComponent>;
    let service: CustomFieldFaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CustomFieldFaComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { snapshot: { queryParams: {} } },
          },
        ],
      })
        .overrideTemplate(CustomFieldFaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CustomFieldFaComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(CustomFieldFaService);

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
      expect(comp.customFields?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
